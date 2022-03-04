## 엔티티를 DTO로 변환

엔티티를 DTO로 변환하는 일반적인 방법이지만 N+1문제가 발생합니다.

```java
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(toList()); //orders를 SimpleOrderDto에 넣고 리스트 형태로 변경
        return result;
    }
    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate; //주문시간
        private OrderStatus orderStatus;
        private Address address;
        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); //lazy초기화 : 영속성 컨텍스트가 member id를 가지고 영속성 컨텍스트를 찾아옴 없으면 db에 쿼리를날림
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
        }
    }
```

위 코드를 실행했을때 코드로만 보면 order와 member,  Delivery쿼리를 한번씩 날리는 것처럼 보이지만 실제로 실행해보면 그렇지 않은 문제 발생합니다.

위 코드를 실행시켰을시 다음과 같은 쿼리가 전송됩니다.

![](https://images.velog.io/images/hope1213/post/4340b6ef-eff1-457d-b4fd-e41d6fd69f0d/image.png)

추가로 아래 2개가 생기는 문제가 발생

![](https://images.velog.io/images/hope1213/post/b777959e-b284-4a50-8c2b-6112de64a5c7/image.png)

list로 받은 order값이 2개일경우

` List<Order> orders = orderRepository.findAllByString(new OrderSearch());`

리스트 안에 2개가 포함되어 반환이 된다

``` 
List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(toList()); 
```

이게 2번 호출됩니다.

첫번째 호출

- orderId = order.getId(); 

  order의 아이디를 찾고

- name = order.getMember().getName();

  order와 연결된 member의 이름을 찾는다

- address = order.getDelivery().getAddress();

  그리고 delivery의 address를 찾는다.

2번째 호출

- order.getMember().getName();

  lazy로딩이 초기화되면서 값을 가져옴

- address = order.getDelivery().getAddress();

  lazy로딩이 초기화되면서 값을 가져옴



총 5번의 쿼리가 나가게 됩니다. 

이렇게 쿼리가 총 1 + N + N번 실행되는 문제가 발생합니다.. (v1과 쿼리수 결과는 같다.)

- order 조회 1번(order 조회 결과 수가 N이 된다.)
- order -> member 지연 로딩 조회 N 번
- order -> delivery 지연 로딩 조회 N 번
  - 예) order의 결과가 4개면 최악의 경우 1 + 4 + 4번 실행됩니다.(최악의 경우)
    - 지연로딩은 영속성 컨텍스트에서 조회하므로, 이미 조회된 경우 쿼리를 생략한다.

> 지연로딩은 기본적으로 N번이기는 한데 N번을 바로 DB쿼리로 보내는게 아니라 영속성컨텍스트에 보냄 만약에 주문한 회원이 다 같은 아이디를 가지고 있다면 member 지연 로딩 조회가 N번이 아니고 1번이 된다
>
> > lazy초기화 : 영속성 컨텍스트가 member id를 가지고 영속성 컨텍스트를 찾아오고 없으면 db에 쿼리를날리기 때문에



## 엔티티를 DTO로 변환 - 페치 조인 최적화



```java
@GetMapping("/api/v3/simple-orders")
public List<SimpleOrderDto> ordersV3() {
    List<Order> orders = orderRepository.findAllWithMemberDelivery();
    List<SimpleOrderDto> result = orders.stream()
        .map(o -> new SimpleOrderDto(o))
        .collect(toList());
    return result;
}
```



```java
public List<Order> findAllWithMemberDelivery() {
    return em.createQuery(
        "select o from Order o" +
        " join fetch o.member m" +
        " join fetch o.delivery d", Order.class)
        .getResultList();
}
```

join fetch 를 사용해서 데이터를 가져온다면 SQL입장에서 조인을 시켜서 한방쿼리로 가져옵니다.

진짜 객체가 orders에 같이 조회가 되서 들어간다.

페치 조인으로 order -> member , order -> delivery 는 이미 조회 된 상태 이므로 지연로딩X



## JPA에서 DTO로 바로 조회



```JAVA
private final OrderSimpleQueryRepository orderSimpleQueryRepository; 
@GetMapping("/api/v4/simple-orders")
public List<OrderSimpleQueryDto> ordersV4() {
    return orderSimpleQueryRepository.findOrderDtos();
}
```

```java
@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {
    private final EntityManager em;
    public List<OrderSimpleQueryDto> findOrderDtos() {
        return em.createQuery(
        "select new
        jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto(o.id, m.name,
        o.orderDate, o.status, d.address)" +
        " from Order o" +
        " join o.member m" +
        " join o.delivery d", OrderSimpleQueryDto.class)
        .getResultList();
    }
}
```

```java
@Data
public class OrderSimpleQueryDto {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate; //주문시간
    private OrderStatus orderStatus;
    private Address address;
    public OrderSimpleQueryDto(Long orderId, String name, LocalDateTime
    orderDate, OrderStatus orderStatus, Address address) {
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
        }
}
```

- 일반적인 SQL을 사용할 때 처럼 원하는 값을 선택해서 조회
- new 명령어를 사용해서 JPQL의 결과를 DTO로 즉시 변환
- SELECT 절에서 원하는 데이터를 직접 선택하므로 DB 애플리케이션 네트웍 용량 최적화(생각보다
  미비)
- 리포지토리 재사용성 떨어짐, API 스펙에 맞춘 코드가 리포지토리에 들어가는 **단점**
  - 리포지토리는 엔티티의 객체그래프를 조회하고 이런거 할때 사용하는데..
- v3보다 재사용성이 떨어짐, 코드상 지저분..



**정리**

엔티티를 DTO로 변환하거나, DTO로 바로 조회하는 두가지 방법은 각각 장단점이 있다. 둘중 상황에
따라서 더 나은 방법을 선택하면 된다. 엔티티로 조회하면 리포지토리 재사용성도 좋고, 개발도 단순해진다.
따라서 권장하는 방법은 다음과 같다.



**쿼리 방식 선택 권장 순서**

1. 우선 엔티티를 DTO로 변환하는 방법을 선택한다.
2. 필요하면 페치 조인으로 성능을 최적화 한다. 대부분의 성능 이슈가 해결된다.
3. 그래도 안되면 DTO로 직접 조회하는 방법을 사용한다.
4. 최후의 방법은 JPA가 제공하는 네이티브 SQL이나 스프링 JDBC Template을 사용해서 SQL을 직접
   사용한다.