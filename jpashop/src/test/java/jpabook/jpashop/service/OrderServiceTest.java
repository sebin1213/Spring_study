package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
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
        Member member = createMember();
        Item item = createBook("시골 JPA", 10000, 10); //이름, 가격, 재고
        int orderCount = 2;

        //when
        Long orderid = orderService.order(member.getId(), item.getId(), orderCount);

        //then
        Order getOrder = orderRepository.findOne(orderid);

        assertEquals(OrderStatus.ORDER, getOrder.getStatus());
        assertEquals(orderid, getOrder.getId());
        assertEquals(1, getOrder.getOrderItems().size()); // 주문한 상품 종류 수
        assertEquals(10000 * orderCount, getOrder.getTotalPrice()); // 주문 가격
        assertEquals(8, item.getStockQuantity());
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
        Member member = createMember();
        Item item = createBook("시골 JPA", 10000, 10); //이름, 가격, 재고
        int orderCount = 20;
        //when,then

        assertThrows(NotEnoughStockException.class,() -> orderService.order(member.getId(), item.getId(), orderCount), "재고 수량 부족 예외가 발생해야 한다.");

    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);
        return member;
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setStockQuantity(stockQuantity);
        book.setPrice(price);
        em.persist(book);
        return book;
    }

}