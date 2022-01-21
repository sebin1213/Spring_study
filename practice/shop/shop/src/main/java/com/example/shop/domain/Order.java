package com.example.shop.domain;

import com.example.shop.domain.Member.Member;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;
    private Long price;

    @Enumerated(EnumType.STRING)
    private OrderStatus Status;



    //==연관관계 편의 메서드==//
    public void setMember(Member member) { //양방향으로 연결
        this.member = member;
        member.getOrders().add(this);  //order와 member를 묶음
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
