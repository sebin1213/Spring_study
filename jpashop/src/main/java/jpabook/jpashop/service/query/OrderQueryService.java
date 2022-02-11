package jpabook.jpashop.service.query;

import jpabook.jpashop.api.OrderApiController;
import jpabook.jpashop.domain.Order;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class OderQueryService {

    public List<OrderApiController.OrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithItem();
        List<OrderApiController.OrderDto> result = orders.stream()
                .map(o -> new OrderApiController.OrderDto(o))
                .collect(toList());
        return result;
    }
}
