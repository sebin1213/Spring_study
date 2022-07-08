package hello.proxy.app.v2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@RequestMapping // controller나 RequestMapping 존재해야 controller로 인식 (RequestMapping 은 @Component 없음)
@ResponseBody
public class OrderControllerV2 {

    private final OrderServiceV2 orderService;
    public OrderControllerV2(OrderServiceV2 orderService) {
        this.orderService = orderService;
    }
    @GetMapping("/v2/request")
    public String request(String itemId) {
        orderService.orderItem(itemId);
        return "ok";
    }
    @GetMapping("/v2/no-log")
    public String noLog() {
        return "ok";
    }
}
