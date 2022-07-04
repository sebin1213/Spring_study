package hello.advanced.app.v1;

import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.hellotrace.HelloTraceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderControllerV1 {
    private final OrderServiceV1 orderService;
    private final HelloTraceV1 trace;

    @GetMapping("/v1/request")
    public String request(String itemId) {
//        TraceStatus status = trace.begin("OrderController.request()");
//        orderService.orderItem(itemId);
//        trace.end(status);

//      위의 과정에서 orderService에서 예외가 터졌을때 함수가 바로 종료되는게 아니라 trace.end가 터져야하지만 바로 종료됨
//        때문에 아래처럼 try catch이용... 하지만 코드 더러움

        TraceStatus status = null;
        try {
            status = trace.begin("OrderController.request()");
            orderService.orderItem(itemId);
            trace.end(status); // 예외가 터져도 해당 함수가 호출되어야함...
            return "ok";
        }
        catch (Exception e){
            trace.exception(status,e);
            throw e; // 예외를 꼭 다시 던져야한다. 안던지면 위에 로그가 안나옴...
        }
    }
}
