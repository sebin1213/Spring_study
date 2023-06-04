package com.example.firstservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/first-service") // Api Gateway에서 경로를 이렇게 설정함
public class FirstServiceController {
    @GetMapping("/welcome")
    public String welcome(){
        return "Welcome to the First service.";
    }
}
