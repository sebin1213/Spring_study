package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.demo.repository.*;
import com.example.demo.service.MemberService;

// 자바 코드로 직접 스프링 빈 등록하기
@Configuration
public class SpringConfig {

    private final MemberRepository memberRepository; //스프링 데이터 JPA 회원 리포지토리를 사용하도록 스프링 설정 변경
    public SpringConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository);
    }
}