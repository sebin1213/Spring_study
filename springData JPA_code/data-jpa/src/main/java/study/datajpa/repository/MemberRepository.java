package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    //인터페이스 메서드에 바로 작성할수있음
    @Query("select m from Member m where m.username= :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int
            age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();  // 값 하나 조회


    //dto로 조회 new 꼭사용 study.datajpa.dto 이렇게 객체를 생성해서 반환하는 문법
    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) " +
            "from Member m join m.team t")
    List<MemberDto> findMemberDto();

    // 컬렉션 조회 :names 에 리스트가 들어가고 해당 리스트에 해당하는 내용이 있으면 검색
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);


    List<Member> findListByUsername(String username); // 컬렉션
    Member findMemberByUsername(String username); //단건
    Optional<Member> findOptionalByUsername(String username);// 단건 optional


    Slice<Member> findSliceByAge(int age, Pageable pageable); // 슬라이스 페이징
    Page<Member> findByAge(int age, Pageable pageable); // 페이징
/** 실무에서는 잘 안사용..?
    // total count 같이 모든 데이터를 가져오고 개수를 세는건 좀 성능이 느림 이런 total count는 성능최적화를 잘해야함
    // 만약에 회원들의 팀을 조회하고 전체 개수를 구한다고 했을때 (모든회원이 팀을 가졌다 가정)
    // 굳이 회원을 팀과 조인하고 그다음 전체개수를 구할 필요가 없음... 그냥 회원의 수를 세면 됨
    // count 쿼리를 분리하는 방법 사용
**/
// 분리
    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m.username) from Member m")
    Page<Member> findTestByAge(int age, Pageable pageable); // 페이징
}

