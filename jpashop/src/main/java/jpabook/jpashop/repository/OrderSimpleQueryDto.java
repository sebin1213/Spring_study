package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;
import java.time.LocalDateTime;
@Data
public class OrderSimpleQueryDto {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate; //주문시간
    private OrderStatus orderStatus;
    private Address address;
    public OrderSimpleQueryDto(Long orderId, String name, LocalDateTime
            orderDate, OrderStatus orderStatus, Address address) { //order엔티티를 못받음 jpa는 식별자로 넣어버기때문에 하나하나 써줘야함 Address나 OrderStatus는 값타입이기 때문에 가능)
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
    }
}