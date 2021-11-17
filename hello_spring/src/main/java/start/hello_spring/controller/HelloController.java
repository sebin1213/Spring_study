package start.hello_spring.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
public class HelloController {
    @GetMapping("hello")
    public String hello(Model model) {
        model.addAttribute("data", "hello!!");
        return "hello";
    }
    ///http://localhost:8080/hello-mvc?name=sss
    @GetMapping("hello-mvc")
    public String helloMvc(@RequestParam("name") String name, Model model) {
        model.addAttribute("name", name);
        return "hello-template";
    }

    // ResponseBody HTTP의 헤드와 바디가 있는데 그 바디부분을 내가 직접 넣어주겠다
    // 굳이 쓰지는 않음ㅋㅋㅋ
    @GetMapping("hello-string")
    @ResponseBody
    public String helloString(@RequestParam("name") String name) {
        return "hello " + name; // view 없음 그냥 문자가 그대로 내려감 페이지 소스보면 그냥 문자만 보임
    }


    // API 형식
    // JSON형식으로 출력
    // 스프링 컨테이너에서 원래는 Viewsolver로 가야하지만 @ResponseBody가 있으면 HttpMessageConverter 동작
    // http 바디에 그대로 데이터를 넘김 근데 문자가 아니고 객체면 default로 json형태로 바꿈
    // 단순 문자 : string converter 동작 /////객체 : json converter 동작
    @GetMapping("hello-api")
    @ResponseBody
    public Hello helloApi(@RequestParam("name") String name) {
        Hello hello = new Hello();
        hello.setName(name);
        return hello; //hello 함수 부르기
    }
    //
    static class Hello {
        private String name;


        // getter setter      alt+insert 단축키
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}