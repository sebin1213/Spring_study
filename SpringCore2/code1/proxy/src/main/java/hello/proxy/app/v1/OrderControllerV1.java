package hello.proxy.app.v1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@RequestMapping // controller나 RequestMapping 존재해야 controller로 인식 (RequestMapping 은 @Component 없음)
@ResponseBody
public interface OrderControllerV1 {

    //인터페이스에는 RequestParam 이것처럼 어노테이션이 있어야 인식이 좀 잘됨...)
    // 자바 버전에따라 인식이 안되는 경우가있음
    @GetMapping("/v1/request")
    String request(@RequestParam("itemId") String itemId);

    @GetMapping("/v1/no-log")
    String noLog();
}
