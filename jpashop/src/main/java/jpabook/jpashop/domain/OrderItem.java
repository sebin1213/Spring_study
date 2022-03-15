package jpabook.jpashop.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;
//import jpabook.jpashop.domain.item.Item;
import javax.persistence.*;

@Entity
@Table(name = "order_item")
@Getter @Setter
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item; //주문 상품

    @JsonIgnore //무한루프 막기위해서
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order; //주문

    private int orderPrice; //주문 가격

    private int count; //주문 수량

    protected OrderItem(){
        // 누가 외부에서 로직 못건들게 함
    }


    //==생성 메서드==//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        // int orderPrice 아이템에 가격이 있는데 이걸 안쓰는 이유는 뭐.. 할인될수도 있으니까?
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);
        item.removeStock(count); // 넘어온 개수만큼 재고를 줄여야함
        return orderItem;
    }



    //==비즈니스 로직 ==//
    /**
    * 재고수량 원복
    **/
    public void cancel(){ //재고수량 원복

        getItem().addStock(count); // 취소한 개수만큼 다시 더해줌
    }

    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}