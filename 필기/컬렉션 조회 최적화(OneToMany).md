# 컬렉션 조회 최적화(OneToMany)

OneToOne이나 ManyToOne 의 경우는 패치조인이나 한방쿼리로 데이터를 한번에 가져와도 크게 성능에 무리를 주지 않지만 컬렉션 조회의 경우 데이터를 조회하는 순간 데이터가 늘어나는 문제가 발생합니다. 때문에 OneToOne이나 ManyToOne 와 달리 해당 경우를 고려하여 성능최적화를 진행해야 합니다.



## 1. 엔티티 직접 노출

```java
@GetMapping("/api/v1/orders")
public List<Order> ordersV1() {
    List<Order> all = orderRepository.findAllByString(new OrderSearch());
    for (Order order : all) {
        order.getMember().getName(); //Lazy 강제 초기화
        order.getDelivery().getAddress(); //Lazy 강제 초기환
        List<OrderItem> orderItems = order.getOrderItems();
        for (OrderItem orderItem : orderItems){
            orderItem.getItem().getName();
        }
    }
    return all;
}
```

orderItem , item 관계를 직접 초기화하면 Hibernate5Module 설정에 의해 엔티티를 JSON으로
생성합니다.
만약 join하는 엔티티가 양방향 연관관계면 무한 루프에 걸리지 않게 한곳에 @JsonIgnore 를 추가해야 합니다.

**단점**

* 엔티티가 변하면 API 스펙이 변한다.
* 트랜잭션 안에서 지연 로딩 필요
* 양방향 연관관계 문제
* 엔티티를 직접 노출한다. (List<OrderItem>)



### 2. 엔티티를 DTO로 변환

```java
@GetMapping("/api/v2/orders")
public List<OrderDto> ordersV2() {
    List<Order> orders = orderRepository.findAllByString(new OrderSearch());
    List<OrderDto> result = orders.stream()
            .map(o -> new OrderDto(o))
            .collect(toList());
    return result;
}
```

- DTO작성

```
@Data//@Getter 사용해도 되지만 그냥 이거 씀
static class OrderDto {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate; //주문시간
    private OrderStatus orderStatus;
    private Address address;
    private List<OrderItemDto> orderItems;
    public OrderDto(Order order) {
        orderId = order.getId();
        name = order.getMember().getName();
        orderDate = order.getOrderDate();
        orderStatus = order.getStatus();
        address = order.getDelivery().getAddress();
        orderItems = order.getOrderItems().stream()
                .map(orderItem -> new OrderItemDto(orderItem))
                .collect(toList());
    }
}
@Data
static class OrderItemDto {
    private String itemName;//상품 명
    private int orderPrice; //주문 가격
    private int count; //주문 수량
    public OrderItemDto(OrderItem orderItem) {
        itemName = orderItem.getItem().getName();
        orderPrice = orderItem.getOrderPrice();
        count = orderItem.getCount();
    }
}
```

출력을 `List<OrderDto>` DTO로 변경하여 엔티티를 노출하는 것을 방지합니다.

**단점**

* 지연 로딩으로 N+1 문제 발생

> **주의사항**
>
> Order만 DTO를 만드는 것이 아니라 Order와 연관관계인 OrderItem또한 DTO로 변경하여 엔티티를 노출하는 것을 방지해야합니다.



### 3. 엔티티를 DTO로 변환 - 페치 조인 최적화



```java
@GetMapping("/api/v3/orders")
public List<OrderDto> ordersV3() {
    List<Order> orders = orderRepository.findAllWithItem();
    List<OrderDto> result = orders.stream()
            .map(o -> new OrderDto(o))
            .collect(toList());
    return result;
}
```

- 조회 변경

```java
public List<Order> findAllWithItem() {
    return em.createQuery(
                    "select distinct o from Order o" +
                            " join fetch o.member m" +
                            " join fetch o.delivery d" +
                            " join fetch o.orderItems oi" +
                            " join fetch oi.item i", Order.class)
            .getResultList();
}
```

페치 조인으로 인해 SQL이 1번만 실행됩니다.



**distinct** 하는 이유

order와 orderitems에서 orderid가 1인 데이터가 orderitems의 id가 4,5인 객체와 연관되어있다고 가정하겠습니다.

만약 이 두 테이블을  JOIN한다면 다음과 같은 결과가 나타날 것입니다.

| order_id | orderitems_id |
| -------- | ------------- |
| 1        | 4             |
| 1        | 5             |

해당 결과는 DB에서는 당연한 이야기지만 JPA에서 데이터를 가져올때 문제가 발생합니다.

예를들어, JOIN한 데이터들의 order_id값이 필요하여 이를 얻겠다는 쿼리가 존재한다고 하겠습니다. order_id는 1이라는 값 하나만 존재해야할 것입니다. 하지만 JPA는 데이터가 2개 존재하기때문에 order_id를 출력할때 2개가 출력되는 현상이 발생합니다. 때문에 distinct를 사용하여 중복데이터를 제거하는 작업을 수행하고 데이터를 출력해줍니다.

> DB의 distinct와 살짝 다릅니다. DB에서의 distinct는 모든 데이터가 같은 값을 가져야 데이터가 제거됩니다. 때문에 해당데이터에서 distinct를 사용한다면  orderitems_id의 값이 다르기때문에 그대로 2개의 값이 나타납니다.
>
> JPA의 distinct는 FROM으로 가져오는 엔티티의 id값만 같다면 중복을 제거합니다. 때문에 위의 데이터에서 사용한다면 1개의 데이터만 출력됩니다.



단점

- 페이징 불가능

> 컬렉션 페치 조인을 사용하면 페이징이 불가능합니다. 
>
> ```
> .setFirstResult(1)
> .setMaxResults(100)
> ```
>
> `findAllWithItem()`에 다음 조건을 추가하여 실행한다면 DB에 보내는 쿼리에 limit(100) offset(1)과같은 조건이 붙어야하지만 실제로는 붙지 않은 것을 확인 할 수 있습니다. 때문에 하이버네이트는 경고 로그를 남기면서 모든 데이터를 DB에서 읽어오고, 메모리에서 페이징처리를 해버립니다. 만약 DB에 데이터가 매우 많다면 모~~든 데이터를 애플리케이션에 올리고 페이징 처리를 해버리기 때문에 Out Of Memory Error가 발생할 수 있습니다.
>
> 추가로 컬렉션 페치 조인은 일대다에대한 패치조인을 하나만 사용해야합니다. 컬렉션 둘 이상에 페치 조인을 사용한다면 데이터가 부정합하게 조회될 수 있습니다.(예를 들어 2개의 데이터를 조회하고 싶어 .setMaxResults(2)라고 조건을 붙였어도 다양한 엔티티와 조인된 결과 값이 나오기 때문에 중복이 매우많이 발생하여 제대로 사용할 수 없는 상황이 발생합니다.)



### 🚀 페이징 + 컬렉션 엔티티를 함께 조회하는 방법



### 3-1. 엔티티를 DTO로 변환 - 페이징과 한계 돌파

컬렉션을 페치 조인하면 일대다 조인이 발생하므로 데이터가 예측할 수 없이 증가하기 때문에 컬렉션을 페치 조인하면 페이징이 불가능합니다. 일다대에서 일(1)을 기준으로 페이징을 하는 것이 목적이다. 그런데 데이터는 다(N)를 기준으로 row가 생성됩니다. 

때문에 Order를 기준으로 페이징 하고 싶어도 다(N)인 OrderItem을 조인하면 OrderItem이 기준이 되어버리는 문제가 발생합니다. 이 경우 하이버네이트는 경고 로그를 남기고 모든 DB 데이터를 읽어서 메모리에서 페이징을 시도하고 에러로 이어질 수 있습니다.



**해결방법**

- ToOne 관계는(OneToOne, ManyToOne) row수를 증가시키지 않으므로 페이징 쿼리에 영향을 주지 않습니다. 때문에 ToOne 관계는 모두 페치조인 합니다.
- 컬렉션은 페지조인을 사용하는 것이 아니라 지연 로딩으로 조회합니다.
- 지연 로딩 성능 최적화를 위해 hibernate.default_batch_fetch_size , @BatchSize 를 적용합니다.
  - hibernate.default_batch_fetch_size: 글로벌 설정 ( 보통 n+1문제가 발생하면 하나씩 데이터를 가져옵니다. 이것을 사용하면 적어둔 갯수만큼 미리 데이터를 가져옵니다.)
  - @BatchSize: 개별 최적화
  - 이 옵션을 사용하면 컬렉션이나, 프록시 객체를 한꺼번에 설정한 size 만큼 IN 쿼리로 조회한다.



조회 수정

```java
public List<Order> findAllWithMemberDelivery(int offset, int limit) {
    return em.createQuery(
                    "select o from Order o" +
                            " join fetch o.member m" +
                            " join fetch o.delivery d", Order.class)
            .setFirstResult(offset)
            .setMaxResults(limit)
            .getResultList();
}
```

order, member, delivery는 패치조인 합니다.(ToOne이니까)

`.setFirstResult(offset)` `.setMaxResults(limit)` 페이징 조건 추가



```java
@GetMapping("/api/v3.1/orders")
public List<OrderDto> ordersV3_page(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                    @RequestParam(value = "limit", defaultValue = "100") int limit) {
    List<Order> orders = orderRepository.findAllWithMemberDelivery(offset,
            limit);
    List<OrderDto> result = orders.stream()
            .map(o -> new OrderDto(o))
            .collect(toList());
    return result;
}
```

그리고 `findAllWithMemberDelivery` 에서 패치한 쿼리를 orders에 저장합니다. orders에 있는 결과값들을 new OrderDto(o)에 넣어 각각의 Order가 루프를 돌며  지연로딩발생하고 프록시 초기화 되면서 연관된 orderitems 가져옵니다. 이안에 들어있는 item개수 만큼 쿼리를 보냅니다. 그리고 이 과정이 반복되면서 결과값을 가져오게 됩니다.



application.properties 추가

```java
spring.jpa.properties.hibernate.default_batch_fetch_size= 100 //in 쿼리 개수
```

`where  orderitems0_.order_id in (?, ?)`

in의 개수를 설정해주는 것입니다. 만약 총데이터가 100건이면 1번 인 쿼리가 날아가고 만약 사이즈를 10으로 설정한다면 10번 날아가게 됩니다.

where절에 in은 pk기반으로 하기 때문에 굉장히 빠릅니다.



> **해당 옵션을 넣는이유**
>
> https://jojoldu.tistory.com/414
>
> **작동원리**
>
> https://velog.io/@jadenkim5179/Spring-defaultbatchfetchsize%EC%9D%98-%EC%9E%91%EB%8F%99%EC%9B%90%EB%A6%AC



#### 장점

- 쿼리 호출 수가 1 + N 1 + 1 로 최적화 된다.

- 조인보다 DB 데이터 전송량이 최적화 된다. (Order와 OrderItem을 조인하면 Order가

- OrderItem 만큼 중복해서 조회된다. 이 방법은 각각 조회하므로 전송해야할 중복 데이터가 없다.)

- 페치 조인 방식과 비교해서 쿼리 호출 수가 약간 증가하지만, DB 데이터 전송량이 감소한다.

- 컬렉션 페치 조인은 페이징이 불가능 하지만 이 방법은 페이징이 가능하다.

  

#### 결론❗

ToOne 관계는 페치 조인해도 페이징에 영향을 주지 않는다. 따라서 ToOne 관계는 페치조인으로 쿼리 수를 줄이고 해결하고, 나머지는 hibernate.default_batch_fetch_size 로 최적화 하자.

>default_batch_fetch_size 의 크기는 적당한 사이즈를 골라야 하는데, 100~1000 사이를 선택하는 것을 권장합니다.
