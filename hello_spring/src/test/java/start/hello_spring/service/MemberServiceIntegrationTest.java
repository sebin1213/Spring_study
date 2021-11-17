// 스프링을 이용해 테스트
// 통합테스트

package start.hello_spring.service;
import org.springframework.test.annotation.Commit;
import start.hello_spring.domain.Member;
import start.hello_spring.repository.MemberRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional // memberRepository.clearStore(); 이거 안해도됨~!~!~!~!
    // 데이터베이스는 기본적으로 트렌젝션이라는 개념이 있음
    // 커밋을 해야 DB에 들어감
    // 테스트를 실행할때 트렌젝션을 실행하고 DB에 데이터를 넣은다음 테스트가 끝나면 롤백을 해줌
class MemberServiceIntegrationTest {
    // 테스트 케이스할때는 필드기반으로 그냥 @Autowired 이걸로 함
    @Autowired MemberService memberService;
    // 구현체는 SpringConfig 여기에서 올라올꺼임
    @Autowired MemberRepository memberRepository;

    @Test
//    @Commit //db에 반영하고싶으면 커밋하면됨
    public void 회원가입() throws Exception {
//Given
        Member member = new Member();
        member.setName("hello");
//When
        Long saveId = memberService.join(member);
//Then
        Member findMember = memberRepository.findById(saveId).get();
        assertEquals(member.getName(), findMember.getName());
    }
    @Test
    public void 중복_회원_예외() throws Exception {
//Given
        Member member1 = new Member();
        member1.setName("spring");
        Member member2 = new Member();
        member2.setName("spring");
//When
        memberService.join(member1);
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> memberService.join(member2));//예외가 발생해야 한다.
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
    }
}