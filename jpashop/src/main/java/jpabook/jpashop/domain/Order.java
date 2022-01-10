package jpabook.jpashop.domain;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders") // order는 데이터베이스 order by의 예약어로 잡고있어서 orders로 변경해줌
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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


    //==생성 메서드==//
    public static Order createOrder(Member member, Delivery delivery,
                                    OrderItem... orderItems) { //OrderItem... 여러개
        Order order = new Order(); // 생성
        order.setMember(member); // 파라미터로 넘어온 member세팅
        order.setDelivery(delivery); // delivery세팅

        for (OrderItem orderItem : orderItems) {   // orderItems에 있는걸 for로 돌림
            order.addOrderItem(orderItem); // orderItems 에 존재하는 물품 모두 추가
        }
        order.setStatus(OrderStatus.ORDER); // 상태
        order.setOrderDate(LocalDateTime.now());  // 현재시각
        return order;
    }


    //==비즈니스 로직==//
    /**
     * 주문 취소
     * */
    public void cancel() { // 주문취소누르면 재고가 다시 올라가야한다.
        if (delivery.getStatus() == DeliveryStatus.COMP) { // 이미 배송을 했으면 취소 불가능 COMP 는 배송완료
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }
        this.setStatus(OrderStatus.CANCEL); // 상태를 취소로 변경
        for (OrderItem orderItem : orderItems) {// 넣었던 상품들 모두 취소
            orderItem.cancel();
        }
    }
    //==조회 로직==//
    /**
     * 전체 주문 가격 조회
     * */
    public int getTotalPrice() {
        int totalPrice = orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
//        for (OrderItem orderItem : orderItems) {
//            totalPrice += orderItem.getTotalPrice();
//        } 해당 식을 스트림으로 변경한 것

        return totalPrice;
    }
}