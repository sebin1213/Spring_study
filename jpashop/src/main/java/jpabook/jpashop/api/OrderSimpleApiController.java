package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderSearch;
import jpabook.jpashop.repository.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.List;
import static java.util.stream.Collectors.toList;
/**
 *
 * xToOne(ManyToOne, OneToOne) 관계 최적화 ********************************
 * Order 를 조회하고
 * Order -> Member order에서 member와의 연관이 되고
 * Order -> Delivery order에서 delivery와 연관
 *
 */

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    private final OrderRepository orderRepository;
    /**
     * V1. 엔티티 직접 노출
     * - Hibernate5Module 모듈 등록, LAZY=null 처리
     * - 양방향 관계 문제 발생 -> @JsonIgnore
     */

    /// 사실 엔티티를 직접노출시킬때 나타나는 문제여서 필요없기는 함.... 그래도 알아두면 좋으니까..
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch()); //new OrderSearch() 그냥 막 가져올라고...? 쌩짜로 가져올라고 이렇게 한다고 합니다...
        //new OrderSearch() 이렇게하면 그냥 그대로 가져오는거기때문에 검색조건이 없음 그래서 주문을 다들고옴
        // 아래를 안쓰면 무한루프에 빠져버림.... member가면 order가져옴 order가면 member부름 무한대....
        for (Order order : all) {
            order.getMember().getName(); //Lazy 강제 초기화
            order.getDelivery().getAddress(); //Lazy 강제 초기환
        }
        return all;
    }



}