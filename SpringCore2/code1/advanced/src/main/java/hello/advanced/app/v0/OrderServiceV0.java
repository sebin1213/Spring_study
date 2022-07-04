package hello.advanced.app.v0;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceV0 {
    private final OrderRepositoryV0 orderRepository;



    // 주문할 상품의 아이디를 입력
    public void orderItem(String itemId) {
        orderRepository.save(itemId);
    }
}