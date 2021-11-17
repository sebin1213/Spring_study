package start.hello_spring.repository;
import org.junit.jupiter.api.Assertions;
import start.hello_spring.domain.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;


class MemoryMemberRepositoryTest {
    MemoryMemberRepository repository = new MemoryMemberRepository();

    @AfterEach //@AfterEach 각 메서드가 끝날때마다 동작하는 콜백함수
    public void afterEach() {
        repository.clearStore(); //test가 끝날때마다 repository를 지워줌
    }

    @Test
    public void save() {
//given
        Member member = new Member();
        member.setName("spring");
//when
        repository.save(member);
//then
        Member result = repository.findById(member.getId()).get();
        // 검증
        //System.out.println("result = " + (result == member));
        //Assertions.assertEquals(member, result);   // 글자로 확인하지는 않지만 초록불들어왔으니 참이라는거
        assertThat(result).isEqualTo(member);
    }
    @Test
    public void findByName() {
//given
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);
        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);
//when
        Member result = repository.findByName("spring1").get();
//then
        assertThat(result).isEqualTo(member1);
    }
    @Test
    public void findAll() {
//given
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);
        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);
//when
        List<Member> result = repository.findAll();
//then
        assertThat(result.size()).isEqualTo(2);
    }
}