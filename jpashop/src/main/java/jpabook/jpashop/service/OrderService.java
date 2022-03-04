package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepositoryOld;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {  // 로직 자체는 정적 팩토리 메소드로 작성했지만 접근할려면 서비스에서 접근

    private final OrderRepository orderRepository;
    private final MemberRepositoryOld memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     * **/

    @Transactional
    public Long order(Long memberId, Long itemId, int count){
        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);

        // 주문상품 생성 ( 일단 하나만 전송 )
        OrderItem orderItem= OrderItem.createOrderItem(item,item.getPrice(),count);

        // 주문 생성
        Order order = Order.createOrder(member,delivery,orderItem);

        // 주문 저장 ( 한번만 save해도 위에 모든것들이 persist 된다 cascade되어있기 때문에)
        orderRepository.save(order); //다른것이 참조할수 없는 프라이빗오너인경우에 사용해야함

        return order.getId();
    }


    /**
     * 주문 취소
     * **/
    @Transactional
    public void cancelOrder(Long orderId){
        // 주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        //주문 취소
        order.cancel(); // 로직을 돌리면 db에 반영되나..? ㅇㅇ 알아서 jpa가 해줌

    }


    //검색

    public List<Order> findOrders(OrderSearch orderSearch) {

        return orderRepository.findAllByString(orderSearch);
    }


}
