package com.example.shop.controller;

import com.example.shop.controller.form.MemberForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("main")
    public String mainShop(Model model){
        model.addAttribute("memberForm", new MemberForm());
        return "main/main";
    }

}
