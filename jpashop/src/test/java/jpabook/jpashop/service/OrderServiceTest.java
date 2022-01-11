package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {
    @Autowired
    OrderService orderService;
    @Autowired
    EntityManager em;
    @Autowired
    OrderRepository orderRepository;

    @Test
    void 상품주문() throws Exception{
        //given
        Member member = new Member();
        member.setName("hope");
        member.setAddress(new Address("서울","거리","1232-22"));
        em.persist(member); // 그냥 넣어버림


        Book book = new Book();
        book.setName("시골 JPA");
        book.setStockQuantity(10);
        book.setPrice(10000);
        em.persist(book);

        int ordercount = 2;

        //when
        Long orderid = orderService.order(member.getId(), book.getId(), ordercount);

        //then
        Order getOrder = orderRepository.findOne(orderid);

        assertEquals(OrderStatus.ORDER, getOrder.getStatus());
        assertEquals(orderid, getOrder.getId());
        assertEquals(1, getOrder.getOrderItems().size()); // 주문한 상품 종류 수
        assertEquals(10000 * ordercount, getOrder.getTotalPrice()); // 주문 가격
        assertEquals(8, book.getStockQuantity());
    }

    @Test
    void 주문취소() throws Exception{
        //given

        //when

        //then
    }

    @Test
    void 상품주문_재고수량초과() throws Exception{
        //given

        //when

        //then
    }

}