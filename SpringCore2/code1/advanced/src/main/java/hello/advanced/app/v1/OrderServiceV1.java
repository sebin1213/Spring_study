package hello.advanced.app.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceV1 {
    private final OrderRepositoryV1 orderRepository;



    // 주문할 상품의 아이디를 입력
    public void orderItem(String itemId) {
        orderRepository.save(itemId);
    }
}