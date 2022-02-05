package com.example.shop.repository;

import com.example.shop.domain.Member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional // 엔티티매니저를 통한 모든 데이터 변경은 트렌젝션 안에서 이뤄져야함
    @Rollback(false)	//테스트 종료 후 데이터를 롤백하지 않고 그대로 남겨두는 옵션
    public void testMember() throws Exception {
        //given
        Member member = new Member();
        member.setUserid("1hoon");
        member.setUserid("hope123");
        member.setPassword("wqeqweq1122");
        member.setEmail("1213hope@gmasil.com");

        //when
        Long savedId = memberRepository.save(member).getId();
        Member findMember = memberRepository.findById(savedId).get();

        //then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember).isEqualTo(member); //JPA 엔티티 동일성 보장
    }

}