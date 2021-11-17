package com.example.demo.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.demo.domain.Member;
import com.example.demo.service.MemberService;

import java.util.List;

// 의존관계: 컨트롤러가 멤버서비스를 통해 회원가입하고 서비스를 통해 데이터를 조회할수 있어야함
// 커늩롤러가 멤서 서비스를 의존함

// @Controller 를 사용하면 스프링이 뜰때 컨테이너에서 관리를 함
@Controller
public class MemberController {
//    스프링이 관리를 하게 되면 스프링 컨테이너에 등록을하고 컨테이너에서 받아서 사용하도록 바꿔야함
//    왜?? new를 사용하게 된다면 다른 멤버 컨트롤러들이 MemberService를 가져다 쓸수있음 예를들어 주문서비스에서 생성해서 가져다쓴다던가...
//    하나만 생성하고 공용으로 사용하는게 좋음
//    private final MemberService memberService = new MemberService();

    private final MemberService memberService;
//    MemberController 는 어차피 컨테이너가 뜰때 생성해줌
//      그러면 생성자를 호출하겠지?? 근데~~ 생성자에  @Autowired가 있으면
//    멤버서비스를 스프링이 스프링컨테이너에 있는 memberService를 가져다가 연결을 해줌
    @Autowired
    public MemberController(MemberService memberService) {

        this.memberService = memberService;

    }
    // 조회
    @GetMapping(value = "/members/new")
    public String createForm() {

        return "members/createMemberForm";
    }
    // form으로 데이터를 넘길때
    @PostMapping(value = "/members/new")
    public String create(MemberForm form){
        Member member = new Member();
        member.setName(form.getName());

        memberService.join(member); // 회원가입 함수
        return "redirect:/"; // home 화면으로 보냄
    }

    @GetMapping(value = "/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }
}
