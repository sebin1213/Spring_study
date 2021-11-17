// 자바 JBM에서 순수 자바코드로 테스트 매우매우 빠르다는 장점
// 이게 훨씬 좋다.... 통합테스트밖에 못한다면 그거는 테스트케이스를 잘못 만든거
// 단위테스트

package start.hello_spring.service;
import org.junit.jupiter.api.BeforeEach;
import start.hello_spring.domain.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import start.hello_spring.repository.MemoryMemberRepository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest {

    MemberService memberService;
    MemoryMemberRepository memberRepository;

//    레파지토리 클리어 하고싶은데 지금 memberService만 존재함 MemoryMemberRepository 안에 clearStore() 함수있음

    @BeforeEach // 그냥 레파지토리를 만들게되면 new에서 다른객체인데 안에 구조를 바꿀위험이있음(지금은 static이라 상관없지만 그래도..)
    public void beforeEach() {
        memberRepository = new MemoryMemberRepository();
        memberService = new MemberService(memberRepository);
    }
    @AfterEach
    public void afterEach() {
        memberRepository.clearStore();
    }

    @Test
    void 회원가입() {
        //Given
        Member member = new Member();
        member.setName("hello");
        //When
        Long saveId = memberService.join(member);
        //Then
        Member findMember = memberService.findOne(saveId).get();
        assertEquals(member.getName(), findMember.getName());
    }

 //    예외상황일때
    @Test
    public void 중복_회원_예외() throws Exception {
        //Given
        Member member1 = new Member();
        member1.setName("spring");
        Member member2 = new Member();
        member2.setName("spring");
        //When
        memberService.join(member1);
        // 이걸로 try catch 하기에는 조금 애매함
//        try {
//            memberService.join(member2);
//            fail();
//        }catch (IllegalStateException e){
//            assertThat(e.getMessage()).isEqualTo("이미~~존재하는 회원입니다.");
//        }

        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> memberService.join(member2));//예외가 발생해야 한다.
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
    }
}