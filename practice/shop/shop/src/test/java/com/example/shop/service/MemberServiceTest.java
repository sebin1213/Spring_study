package com.example.shop.service;

import com.example.shop.domain.Member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Test
    @Rollback(false)
    public void testMemberJoin() throws Exception {
        //Given
        Member member = new Member();
        member.setUsername("sebi2n");
        member.setUserid("hope123");
        member.setPassword("wqeqweq1122");
        member.setEmail("1213hope@gmasil.com");
        //When
        Long saveId = memberService.join(member);
        //Then
        Member findMember = memberService.findOne(saveId).get();
        assertEquals(member.getUsername(), findMember.getUsername());
    }
}