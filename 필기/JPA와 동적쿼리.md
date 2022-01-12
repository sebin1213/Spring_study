# JPA와 동적쿼리

>[실전 스프링부터 jpa활용](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8-JPA-%ED%99%9C%EC%9A%A9-1/dashboard) 

### 동적쿼리가 필요한 이유

```python
@Getter @Setter
public class OrderSearch {
    private String memberName; //회원 이름
    private OrderStatus orderStatus;//주문 상태[ORDER, CANCEL]
}
```

Order 라는 주문한 상품을 담고있는 엔티티가 존재하고 상품의 상태와 상품을 주문한 회원이름으로 상품을 검색하는 쿼리를 만든다고 가정하겠습니다. 

해당 조건을 모두 만족시키는 로직을 작성한다면 다음과 같이 나올 것입니다.

```python
public List<Order> findAll(OrderSearch orderSearch){
    return em.createQuery("select 0 from Order o join o.member m" +
            "where 0.status = :status" +
            "and m.name like :name",Order.class)
            .setParameter("status",orderSearch.getOrderStatus())
            .setParameter("name", orderSearch.getMemberName())
            .setMaxResults(1000)
            .getResultList();
```

`select 0 from Order o join o.member m` order와 member를 조인하고 

`where 0.status = :status` 검색하고자하는 상품의 상태와

`m.name like :name`  상품을 주문한 회원의 이름을 

`.setParameter("status",orderSearch.getOrderStatus())`

`.setParameter("name", orderSearch.getMemberName())` 파라미터를 통해 입력받아

`setMaxResults(1000)` 최대 1000개의 주문상품을 보여줍니다.

하지만 아무 값도 입력하지 않고 **단순히 검색버튼만 눌러 전체 데이터를 보고싶다면 ** 위의 쿼리로는 값이 입력되지 않았기때문에 에러가 발생하며 다음과 같은 쿼리가 필요합니다.

```python
public List<Order> findAll(OrderSearch orderSearch){
    return em.createQuery("select 0 from Order o join o.member m" ,Order.class)
            .setMaxResults(1000)
            .getResultList();
```

또한 **상품을 주문한 회원의 이름으로만 검색 **하여 데이터를 확인할때도 **상품의 상태만**으로 검색을 할때도 각각 다른 쿼리가 필요합니다.



**결과적으로 검색하고자하는 조건에 따라 다른 쿼리가 필요한 사태가 발생하고 때문에 동적쿼리를 이용해 이를 해결해야합니다.**



---

## 해결방법



- JPQL로 해결
- JPA Criteria로 처리

- 👍👍**Querydsl**

  

### JPQL로 해결

> 매우 힘든 방법.. 무식한 방법

```PYTHON
public List<Order> findAllByString(OrderSearch orderSearch) {
    String jpql = "select o From Order o join o.member m";
    boolean isFirstCondition = true;
    
    //주문 상태 검색
    if (orderSearch.getOrderStatus() != null) {
        if (isFirstCondition) {
            jpql += " where";
            isFirstCondition = false;
        } 
        else {
            jpql += " and";
        }
        jpql += " o.status = :status";
    }
    //회원 이름 검색
    if (StringUtils.hasText(orderSearch.getMemberName())) {
        if (isFirstCondition) {
            jpql += " where";
            isFirstCondition = false;
        } 
        else {
            jpql += " and";
        }
        jpql += " m.name like :name";
    }
    TypedQuery<Order> query = em.createQuery(jpql, Order.class)
    .setMaxResults(1000);
    
    if (orderSearch.getOrderStatus() != null) {
        query = query.setParameter("status", orderSearch.getOrderStatus());
    }
    if (StringUtils.hasText(orderSearch.getMemberName())) {
        query = query.setParameter("name", orderSearch.getMemberName());
    }
    return query.getResultList();
}
```

해당 로직을 이용한다면  **상품을 주문한 회원의 이름으로만 검색 **하는 경우에도 문제없이 작동합니다.

`if (orderSearch.getOrderStatus() != null)` 상태를 확인하는 파라미터가 NULL이라면 해당 로직을 작동하지 않습니다.

`if (StringUtils.hasText(orderSearch.getMemberName()))` 회원이름 파라미터를 가지고있을때만 해당 로직이 실행됩니다.

 `if (isFirstCondition)` where가 붙을 조건을 선택해줍니다. 

만약 상태를 확인하는 파라미터가 존재할 경우 `where 0.status = :status`로 시작되지만 

상태확인 파라미터가 존재하지않고 회원이름 파라미터만 존재할 경우 `where m.name like :name`로 쿼리가 시작됩니다.

또한 파라미터 바인딩도 동적으로 해야하므로 조건이 많다면 JPQL로 해결하기에는 매우 복잡해지고 실수로인한 버그가 발생할 가능성이 높습니다.



### JPA Criteria로 처리

**JPA Criteria** : JPA가 JPQL을 자바코드로 작성할때 표준으로 제공하는 빌더 클래스 API

>JPA에서 표준스펙으로 제공하지만 잘 사용하지 않습니다. 그리고 공부하지 않아 설명하지 못합니다...

```python
public List<Order> findAllByCriteria(OrderSearch orderSearch) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Order> cq = cb.createQuery(Order.class);
    Root<Order> o = cq.from(Order.class);
    
    Join<Order, Member> m = o.join("member", JoinType.INNER); //회원과 조인
    
    List<Predicate> criteria = new ArrayList<>();
    
    //주문 상태 검색
    if (orderSearch.getOrderStatus() != null) {
        Predicate status = cb.equal(o.get("status"),
        orderSearch.getOrderStatus());
        criteria.add(status);
    }
    //회원 이름 검색
    if (StringUtils.hasText(orderSearch.getMemberName())) {
        Predicate name = cb.like(m.<String>get("name"), "%" +orderSearch.getMemberName() + "%");
        criteria.add(name);
    }
    cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
    TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);
    
    return query.getResultList();
}
```

JPA Criteria를 사용하지 않는 대표적인 이유는 유지보수성이 매우 떨어집니다. 

해당 코드를 보면 and조건으로 조건들이 다양하게 합쳐지지만 뭐가 만들어지는지 한번에 떠오르지않고 감이 오지 않습니다. 때문에 다른대안으로 동적쿼리를 해결합니다.



### 👍Querydsl

Querydsl은 쿼리를 문자가 아니라 자바 코드로 작성할 수 있게 도와주며 동적 쿼리 문제를 깔끔하게 해결합니다. 또한 문법 오류도 컴파일 시점에 모두 잡아주는 장점이 있습니다.

> [Querydsl 설치 방법](https://wangtak.tistory.com/m/12)

```python
public List<Order> findAll(OrderSearch orderSearch){
    QOrder order = QOrder.order;
    QMember member = QMember.member;

    return query
            .select(order)
            .from(order)
            .join(order.member, member)
            .where(statusEq(orderSearch.getOrderStatus()),
                    nameLike(orderSearch.getMemberName()))
            .limit(1000)
            .fetch();
}
private BooleanExpressionPredicate statusEq(OrderStatus statusCond){
    if(statusCond == null){
        return null;
    }
    return order.status.eq(statusCond);
}
private BooleanExpressionPredicate nameLike(String nameCond){
    if (!StringUtils.hasText(nameCond)){
        return null;
    }
    return member.name.like(nameCond)
}
```

