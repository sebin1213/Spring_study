package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController //json 데이터 전송 Controller는 view반환
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;
    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    @GetMapping("/members2/{id}")
    public String findMember(@PathVariable("id") Member member) {
        return member.getUsername();
    }


//    @GetMapping("/members")
//    public Page<Member> list(@PageableDefault(size = 5) Pageable pageable) {
//        Page<Member> page = memberRepository.findAll(pageable);
//        return page;
//    }

    @GetMapping("/members")
    public Page<MemberDto> list(Pageable pageable) {
        Page<Member> page = memberRepository.findAll(pageable);
//        Page<MemberDto> pageDto = page.map(member -> new MemberDto(member.getId(),member.getUsername(),null));
        Page<MemberDto> pageDto = page.map(MemberDto::new);
        return pageDto;

        // 위 코드를 더 최적화하면 아래와 같음
        // return memberRepository.findAll(pageable).map(MemberDto::new);
    }


    @PostConstruct
    public void init(){
        for (int i=0;i<100;i++){
            memberRepository.save(new Member("USER"+i,i));
        }

    }
}