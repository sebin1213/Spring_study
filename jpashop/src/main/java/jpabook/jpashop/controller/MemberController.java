package jpabook.jpashop.controller;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping(value = "/members/new") // MemberForm()을 실어서 넘김
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping(value = "/members/new") // 데이터를 받아 등록하는 것 MemberForm form이 파라미터로 넘어옴
    public String create(@Valid MemberForm form, BindingResult result) { // @Valid 필수로 사용하겟다, BindingResult result 오류가 발생할시 아래 실행
        if (result.hasErrors()) {
            return "members/createMemberForm";
        }
        Address address = new Address(form.getCity(), form.getStreet(),
                form.getZipcode());
        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);
        memberService.join(member);
        return "redirect:/"; // 첫번째 페이지로 넘어감
    }

    @GetMapping(value = "/members")
    public String list(Model model) {
        //  List<Member> members 사실이렇게 하면 안됨.. MemberForm form 이거나 dto로 변환해서 사용해야함
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/MemberList";
    }
}