package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // 트랜젝션 커밋을 안하고 그냥 롤백함
class MemberServiceTest
{
    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Rollback(false) // 메세지 보고싶을때
    void 회원가입() throws Exception
    {
        //given
        Member member = new Member();
        member.setName("1hoon"); //persist만 하고 db insert는 안함 커밋될때 그때 됨

        //when
        Long joinId = memberService.join(member);


        //then
        assertEquals(member, memberRepository.findOne(joinId));
    }

    @Test
    void 중복_회원_예외() throws Exception
    {
        //given
        String name = "1hoon";

        Member memberA = new Member();
        memberA.setName(name);

        Member memberB = new Member();
        memberB.setName(name);

        //when
        memberService.join(memberA);

        //then
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> memberService.join(memberB));
        assertEquals("이미 존재하는 회원입니다.", thrown.getMessage());
    }

    @Test
    void 트랜젝션이_언제까지살아있는가() throws Exception{
        Member m = memberService.findOne(1L);
        memberService.findMembers();
    }
}