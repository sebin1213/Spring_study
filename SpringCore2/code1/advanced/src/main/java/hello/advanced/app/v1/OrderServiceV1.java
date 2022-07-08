package hello.advanced.app.v1;

import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.hellotrace.HelloTraceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceV1 {
    private final OrderRepositoryV1 orderRepository;
    private final HelloTraceV1 trace;



    // 주문할 상품의 아이디를 입력
    public void orderItem(String itemId) {
        TraceStatus status = null;
        try {
            status = trace.begin("OrderService.orderItem()");
            orderRepository.save(itemId);
            trace.end(status); // 예외가 터져도 해당 함수가 호출되어야함...
        }
        catch (Exception e){
            trace.exception(status,e);
            throw e; // 예외를 꼭 다시 던져야한다. 안던지면 위에 로그가 안나옴...
        }
    }
}