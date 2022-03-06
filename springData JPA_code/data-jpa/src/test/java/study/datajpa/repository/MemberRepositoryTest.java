package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);
        Member findMember =
                memberRepository.findById(savedMember.getId()).get();
        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember).isEqualTo(member); //JPA 엔티티 동일성보장
    }
    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);
//단건 조회 검증

        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        Assertions.assertThat(findMember1).isEqualTo(member1);
        Assertions.assertThat(findMember2).isEqualTo(member2);
//리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        Assertions.assertThat(all.size()).isEqualTo(2);
//카운트 검증
        long count = memberRepository.count();
        Assertions.assertThat(count).isEqualTo(2);
//삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);
        long deletedCount = memberRepository.count();
        Assertions.assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThan() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);
        List<Member> result =
                memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);
        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        Assertions.assertThat(result.get(0).getAge()).isEqualTo(20);
        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void testQuery() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);
        List<Member> result =
                memberRepository.findUser("AAA", 15);
        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        Assertions.assertThat(result.get(0).getAge()).isEqualTo(20);
        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void findUserNameList() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);
        List<String> usernameList =
                memberRepository.findUsernameList();
        for (String s : usernameList){
            System.out.println("s = " + s);
        }
    }

    @Test
    public void findMemberDto() {
        Team team = new Team("teamaaa");
        teamRepository.save(team);

        Member m1 = new Member("AAA", 10);
        memberRepository.save(m1);

        List<MemberDto> memberDto =
                memberRepository.findMemberDto();
        for (MemberDto m : memberDto){
            System.out.println("m = " + m);
        }
    }


    @Test
    public void findMemberList() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);
        List<Member> usernameList = memberRepository.findByNames(Arrays.asList("AAA","BBB"));
        for (Member s : usernameList){
            System.out.println("s = " + s);
        }
    }

    @Test
    public void returnType(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);
//        Member aaa = memberRepository.findMemberByUsername("AAA"); //스프링 데이터 JPA 는 값없어도 NULL 반환 그냥 JPA는 NoResultException
//        Member aaa = memberRepository.findMemberByUsername("AAA"); //현재는 에러남 2개이상의 데이터를 넣었는데 하나만 출력하라고 해서..
        List<Member> bbb = memberRepository.findListByUsername("AAAff");
//        System.out.println("size???" + bbb.size()); // 사이즈가 0으로 나옴 존재하지 않아도 null로 반환함 이제는 그냥 optional로 사용함
    }
    @Test
    public void paging() throws Exception {
//given

        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age= 10;
        PageRequest pageRequest = PageRequest.of(0,3, Sort.by(Sort.Direction.DESC,"username"));
        // 0 페이지부터 3개 가져오고 유저이름으로 정렬
//when

        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        // api로 보낼때는 당연히 이렇게 dto형태로 바꿔야함
        Page<MemberDto> toMap =  page.map(member -> new MemberDto(member.getId(),member.getUsername(),null));

        //then
        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();

        for ( Member member : content){
            System.out.println("member = " + member);
        }
        System.out.println("totalElements = " + totalElements);

        Assertions.assertThat(content.size()).isEqualTo(3);
        Assertions.assertThat(page.getTotalElements()).isEqualTo(5);
        Assertions.assertThat(page.getNumber()).isEqualTo(0); // 현재 몇번째 페이지인지 알아서 제공해줌
        Assertions.assertThat(page.getTotalPages()).isEqualTo(2); // 전체 몇페이지인가
        Assertions.assertThat(page.isFirst()).isTrue();// 첫번째 페이지인가?
        Assertions.assertThat(page.hasNext()).isTrue(); // 다음 페이지가 존재하는가?

        // 실무에서는 잘 안사용..?
        // total count 같이 모든 데이터를 가져오고 개수를 세는건 좀 성능이 느림 이런 total count는 성능최적화를 잘해야함
        // 만약에 회원들의 팀을 조회하고 전체 개수를 구한다고 했을때 (모든회원이 팀을 가졌다 가정)
        // 굳이 회원을 팀과 조인하고 그다음 전체개수를 구할 필요가 없음... 그냥 회원의 수를 세면 됨
        // count 쿼리를 분리하는 방법 사용
    }

    @Test
    public void slice_paging() throws Exception {
//given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age= 10;
        PageRequest pageRequest = PageRequest.of(0,3, Sort.by(Sort.Direction.DESC,"username"));
        // 0 페이지부터 3개 가져오고 유저이름으로 정렬
//when
        Slice<Member> slice_page = memberRepository.findSliceByAge(age, pageRequest);
        // 0번째에서 3번째까지 가져오라고 요청했지만 0부터 4개까지 가져옴 다음값이 있는지 단순히 확인하는거임 ( 모바일 환겨에서 많이 사용하겠지..?

        //then
        List<Member> content2 = slice_page.getContent();
        Assertions.assertThat(content2.size()).isEqualTo(3);
        Assertions.assertThat(slice_page.getNumber()).isEqualTo(0); // 현재 몇번째 페이지인지 알아서 제공해줌
        Assertions.assertThat(slice_page.isFirst()).isTrue();// 첫번째 페이지인가?
        Assertions.assertThat(slice_page.hasNext()).isTrue(); // 다음 페이지가 존재하는가?
    }
}