package jpabook.jpashop;

import jpabook.jpashop.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

//import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
public class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional // 엔티티매니저를 통한 모든 데이터 변경은 트렌젝션 안에서 이뤄져야함
	@Rollback(false)	//테스트 종료 후 데이터를 롤백하지 않고 그대로 남겨두는 옵션
    public void testMember() throws Exception {
        //given
        Member member = new Member();
        member.setUsername("1hoon");

        //when
        Long savedId = memberRepository.save(member);
        Member findMember = memberRepository.find(savedId);

        //then
//        assertEquals(findMember.getId(), member.getId());
//        assertEquals(findMember.getUsername(), member.getUsername());
//        assertEquals(findMember, member);
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member); //JPA 엔티티 동일성 보장

    }
}