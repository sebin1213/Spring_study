package jpabook.jpashop.domain;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders") // order는 데이터베이스 order by의 예약어로 잡고있어서 orders로 변경해줌
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //member와 다대일관계
    @JoinColumn(name = "member_id") //매핑을 뭐로할지 외래키(연관관계 주인이니까 order에 써줌)
    private Member member; //주문 회원

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY) //1대1일때는 아무나 외래키를 가질수 있지만 자주 엑세스되는곳에 둔다면 효율이 더 좋아질듯..?
    @JoinColumn(name = "delivery_id")
    private Delivery delivery; //배송정보

    private LocalDateTime orderDate; //주문시간   // 예전에는 Date date로 하고 어노테이션으로 매핑했지만 지금은 하이버 네이트가 관리해줌

    @Enumerated(EnumType.STRING)
    private OrderStatus status; //주문상태 [ORDER, CANCEL]

    //==연관관계 편의 메서드==//
    public void setMember(Member member) { //양방향으로 연결
        this.member = member;
        member.getOrders().add(this);  //order와 member를 묶음
        // 아래 코드를 간단하게 위에 코드로 나타낸거
//        Member member = new Member();
//        Order order = new Order();
//
//        member.getOrders().add(order);
//        order.setMember(member);
    }
    public void addOrderItem(OrderItem orderItem) { //order랑 orderitem도 양방향
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }
}