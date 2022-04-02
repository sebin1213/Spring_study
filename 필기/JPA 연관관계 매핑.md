#  JPA, 하이버네이트 연관관계 매핑



다음과 같은 주문, 상품이라는 엔티티가 존재한다고 가정하겠습니다.

```java
@Entity
@Getter @Setter
public class Order{
	@Id @ GeneratedValue
	private Long id;
	private String name;
	
	@OneToMany(mappedBy = "order")
	private List<item> items = new ArrayList<>();
	
	void add(Item item){
		this.items.add(item);
	}
}
```

```java
@Entity
@Getter @Setter
public class Item{
	@Id @ GeneratedValue
	private Long id;
	private String name;
	
	@ManyToOne
	private Order order
	
}
```

현재 이 엔티티들의 관계는 일대다 관계로 하나의 주문에 여러개의 상품이 담길 수 있고 이들을 양방향관계로 연결한 것을 볼 수 있습니다. 

하지만 이 관계에는 문제가 하나 존재합니다. 

```java
@Test
public void createItem{
	Order order = new Order();
	order.setName("1번 주문")
	orderRepository.save(order);
	
	Item item = new Item();
	item.setName("감자");
	
	order.add(item);
	orderRepository.save(item);
}
```

해당코드를 보면 주문을 생성하여 값을 넣고 이를 저장합니다. 그리고 상품의 이름을 넣어주고 방금 저장한 주문에 상품의 정보를 넣어줍니다. 이렇게 한다면 문제없이 주문엔티티에 상품의 정보가 들어간 것처럼 보입니다. 하지만 실제 DB를 확인한다면 예상과는 다른 결과가 나타납니다.



**주문DB**

| ID   | NAME     |
| ---- | -------- |
| 1    | 1번 주문 |

**상품DB**

| ID   | NAME | ORDER_ID |
| ---- | ---- | -------- |
| 2    | 감자 | NULL     |

분명 테스트 코드를 통해 주문에 상품 데이터를 넣었으니 DB에서도 이 상품데이터가 나타나야하지만 나타나지 않은 것을 확인할 수있습니다. 

왜 이러한 결과값이 나타는 걸까요? 이를 알기위해서는 @OneToMany 에 대해 알아야합니다. 


여기서 mappedBy의 역할을 알게 됩니다. 서로다른 엔티티를 양방향 관계로 해주고 연관관계의 주인을 설정(반대쪽에서 현재엔티티를 참조하고있다.)해주는 것을 알수있습니다.

이 관계의 주인은 ITEM 상품인데 상품에는 관계를 설정하지 않고 자기 자신에게만 상태를 변경시켜주고 있습니다.

 그렇게된다면 객체가 변경됬음에도 불구하고 관계의 주인쪽에 아무런 변화가 일어나지 않았기 때문에 아무런 변경이 일어나지 않게 됩니다.

```java
@Entity
@Getter @Setter
public class Order{
	@Id @ GeneratedValue
	private Long id;
	private String name;
	
	@OneToMany(mappedBy = "order")
	private List<item> items = new ArrayList<>();
	
	void add(Item item){
        item.setItem(this);
		getItems().add(item);
	}
}
```



만약 위의 엔티티가 @OneToMany(mappedBy = "order")가 아닌 @OneToMany 로 변경한다면 어떤 결과값이 나타날까요? @OneToMany, @ManyToOne 이 관계는 양방향이 아닌 서로 다른 2개의 단방향 관계로 변경됩니다. 그리고 이를 DB에서 확인하자면 다음과 같이 나오게 됩니다.


**주문DB**

| ID   | NAME     |
| ---- | -------- |
| 1    | 1번 주문 |

**상품DB**

| ID   | NAME | ORDER_ID |
| ---- | ---- | -------- |
| 2    | 감자 | NULL     |

**주문_상품DB**

| ORDER_ID | ITEM_ID |
| -------- | ------- |
| 1        | 2       |

새로운 DB인 **주문_상품DB** 가 새로 생성됩니다. 이 DB가 생성된 이유는 하이버네이트의 기본 매핑 방법





getItems().add(item);  객체지향적으로 사용하기위해 객체지향적인 시각에서 관계를 맺을때 당연히 두쪽다 설정이되야하는게 맞아 db에 반영이 안되는데 어쩌구하냐

여기에서 한가지 의문이 들었습니다.

```java
	void add(Item item){
        item.setItem(this);
		getItems().add(item);
	}
```

해당 코드를보면 item.setItem(this); 는 외래키를 넣어주는 역할을 합니다. DB관점에서 생각했을때 두개의 테이블은 하나의 외래키를 가지고 서로 데이터를 주고받습니다. 근데 왜 객체에서는 getItems().add(item); 을 사용하는여 양쪽을 연결시켜주는지 의문이였습니다.



객체지향을 내가 잘 못랐음,....ㅎㅎ

이를 알기위해서는 DB 와 객체의 차이점을 알아야합니다

DB는 데이터의 어떻게 정규화해서 저장할지에 초점이 맞춰져있습니다.

OOP는 데이터에 초점을 맞춘것이아니라 어떻게하면 관리를 잘하고 추상화를 잘할지 여기에 초점을 맞춰서 개발된 것입니다.



예를 들어 DB에 주문, 상품테이블이 존재하고 이 테이블과 관련된 아래와 같은 요구사항이 들어왔다 가정하겠습니다.

`1번주문에 들어있는 상품을 출력해주세요`

`상품 1번이 들어있는 주문을 알려주세요`

해당 요구사항을 만족시키기 위해서는 두 테이블 모두 외래키를 통해 JOIN하게됩니다. 이말은 즉슨 외래키가 테이블 하나에 존재하더라도 양방향으로 접근이 가능하다는 말이 됩니다.



하지만 객제의 경우 조금 다릅니다.
