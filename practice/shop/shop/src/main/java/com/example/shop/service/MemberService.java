package com.example.shop.service;

import com.example.shop.domain.Member.Member;
import com.example.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Long join(Member member){
//        validateDuplicateMember(member); // 중복 아이디,이메일 검증
        memberRepository.save(member);
        return member.getId();
    }

    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }
    public List<Member> findAll(Long memberId) {
        return memberRepository.findAll();
    }


    private void validateDuplicateMember(Member member) {
        System.out.println(memberRepository.findByUserId(member.getUserid()));
        Optional<Member> findMemberUserId = memberRepository.findByUserId(member.getUserid());
        if (!findMemberUserId.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }
        Optional<Member> findMemberEmail = memberRepository.findByEmail(member.getUserid());
        if (!findMemberEmail.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }
    }
}
