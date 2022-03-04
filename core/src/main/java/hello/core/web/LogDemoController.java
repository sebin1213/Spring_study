package hello.core.web;
import hello.core.common.MyLogger;
import hello.core.web.LogDemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class LogDemoController {

    private final LogDemoService logDemoService;
    private final ObjectProvider<MyLogger> myLoggerProvider;

    @RequestMapping("log-demo") // log-demo라는 요청이오면
    @ResponseBody // 화면을 지금 안만들어서 문자로 반환할꺼임
    public String logDemo(HttpServletRequest request) { // 자바에서 표준 서블릿규약에 의해서 반환받을수 있음
        MyLogger myLogger = myLoggerProvider.getObject();

        String requestURL = request.getRequestURL().toString();
        myLogger.setRequestURL(requestURL); //이거 왜함..? 로그남길때 주소까지 남길라고
        myLogger.log("controller test");
        logDemoService.logic("testId");
        return "OK";
    }
}