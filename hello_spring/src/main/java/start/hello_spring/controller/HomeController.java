package start.hello_spring.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller

public class HomeController {
    @GetMapping("/") //그냥 localhost 8080
    public String home() {
        return "home";
    }
}