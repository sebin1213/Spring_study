package com.example.shop.controller;

import com.example.shop.controller.form.MemberForm;
import com.example.shop.domain.Address;
import com.example.shop.domain.Member.Member;
import com.example.shop.repository.MemberRepository;
import com.example.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MemberJoinController {
    public final MemberService memberService;


    @GetMapping(value = "/members/new")
    public String createMemberForm(Model model){
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping(value = "/members/new")
    public String createMemberPost(@Valid MemberForm form, BindingResult result) { //BindingResult result 오류가 발생할시 아래 실행
        if (result.hasErrors()) {
            return "members/createMemberForm";
        }
        Member member = new Member();
        member.setUserid(form.getUserid());
        member.setPassword(form.getPassword());
        member.setUsername(form.getUsername());
        member.setEmail(form.getEmail());
        memberService.join(member);
        return "redirect:/"; // 첫번째 페이지로 넘어감
    }
}
