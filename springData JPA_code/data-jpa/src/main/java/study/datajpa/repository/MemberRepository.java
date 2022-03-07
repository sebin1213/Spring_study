package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>,MemberRepositoryCustom {
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
// 분리해서 동작별로 쿼리를 짬
    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m.username) from Member m")
    Page<Member> findTestByAge(int age, Pageable pageable); // 페이징

    //반환타입은 int
    // 쿼리들을 그냥 반환하면 .getSingleResult();이거나 .getResultList(); 이런걸로 반환함
    // 우리는 수정 삭제할꺼니까 조회 쿼리를 제외하고 데이터에 변경이 필요할때 사용하는 @Modifying이 필요함
    @Modifying//(clearAutomatically = true) // 해당 어노테이션이 없다면 오류발생
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);
    /**
     * jpa라는건 영속성 컨텍스트에서 엔티티를 관리해야함.. 하지만 위의 코드(벌크연산)는 db에 접근하여 그냥 데이터를 변경하는 코드
     * 테스트 코드의 member 저장하는 코드를 보면 save를 통해 영속성 컨텍스트에 들어가는 것을 볼수 있음
     * (참고로 jpa는 업데이트 등등... 하는 쿼리(jpql)가 나올시 이전에 영속성 컨텍스트에 존재하는 해당 JPQL과 관련 있는 엔티티를 플러시한다.)
     * 위의 연산은 db에 있는 내용을 수정하기 때문에 영속성 컨텍스트내에 있는 엔티티는 여전히 변경전의 값을 가짐
     * 영속성 컨텍스트를 벌크연산이 끝날때마다 초기화함 clearAutomatically = true
     *
     * **/


    @Query("select m from Member m left join fetch m.team") // 한번에 묶인애들 다 가져옴
    List<Member> findMemberFetchJoin();


    //공통 메서드 오버라이드
    // member를 조회하면서 team까지 조회하고싶어..!! fetch조인을 사용하고 싶지만 jpal쓰는게 귀찮아..!!
    // fetch조인을 사용하면서 편하게 쓰기... 이 기능은 jpa에서 제공하는 기능

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    //JPQL + 엔티티 그래프
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    //메서드 이름으로 쿼리에서 특히 편리하다.
    @EntityGraph(attributePaths = {"team"})
    List<Member> findByUsername(String username);

    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value =
            "true"))
    Member findReadOnlyByUsername(String username);

//    @Lock(LockModeType.PESSIMISTIC_WRITE) // 함부로 손대지마..!!
//    List<Member> findByUsername(String name);
}

