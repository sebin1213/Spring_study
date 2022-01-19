package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderSearch;
import jpabook.jpashop.domain.OrderStatus;
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
            order.getMember().getName(); //Lazy 강제 초기화     lazy로딩이란 order에서 getmember를 가지고 올때까지는 프록시 객체임 db에 쿼리가 안날아간 상태, 진짜 객체가 아니라 가짜로 만들어지것임 근데 .getName();을 하면 실제 name을 가지고 와야하니 db가 강제로 초기화됨
            order.getDelivery().getAddress(); //Lazy 강제 초기화
        }
        return all;
    }


    /**
     * V2. 엔티티를 조회해서 DTO로 변환(fetch join 사용X)
     * - 단점: 지연로딩으로 쿼리 N번 호출
     */
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(toList()); //orders를 SimpleOrderDto에 넣고 리스트 형태로 변경
        return result;
    }
    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate; //주문시간
        private OrderStatus orderStatus;
        private Address address;
        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); //lazy초기화 : 영속성 컨텍스트가 member id를 가지고 영속성 컨텍스트를 찾아옴 없으면 db에 쿼리를날림
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
        }
    }


    /**
     * V3. 엔티티를 조회해서 DTO로 변환(fetch join 사용O)
     * - fetch join으로 쿼리 1번 호출
     * 참고: fetch join에 대한 자세한 내용은 JPA 기본편 참고(정말 중요함)
     */
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(toList());
        return result;
    }






    private final OrderSimpleQueryRepository orderSimpleQueryRepository; //의존관계주입
/**
 * V4. JPA에서 DTO로 바로 조회
 * - 쿼리 1번 호출
 * - select 절에서 원하는 데이터만 선택해서 조회
 */
    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4() {
        return orderSimpleQueryRepository.findOrderDtos();
    }

}