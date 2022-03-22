package study.datajpa.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.repository.MemberRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MemberTest {
    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void testEntity() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
//초기화
        em.flush();
        em.clear();
//확인
        List<Member> members = em.createQuery("select m from Member m",
                        Member.class)
                .getResultList();
        for (Member member : members) {
            System.out.println("member=" + member);
            System.out.println("-> member.team=" + member.getTeam());
        }
    }

    @Test
    @Transactional
    public void JpaEventBaseEntity() throws Exception {
//given
        Member member = new Member("member1");
        memberRepository.save(member); //@PrePersist 발생 (트렌젝션 어노테이션 없어도 동작함 save를 구현한 코드에 이미 트렌젝셔널이 붙어있어서 대신에 save하고나면 트렌젝션이 끝나기 때문에 영속성 컨텍스트에서 사라짐..?(osiv 공부)
        Thread.sleep(100); // 잠깐 쉬기(원래는 사용안함)

        member.setUsername("member2");
        em.flush(); //@PreUpdate 발생
        em.clear();
//when
        Member findMember = memberRepository.findById(member.getId()).get();
//then
        System.out.println("findMember.createdDate = " +
                findMember.getCreatedDate());
//        System.out.println("findMember.updatedDate = " +
//                findMember.getUpdatedDate());
        System.out.println("findMember.updatedDate = " +
                findMember.getLastModifiedDate());
//        System.out.println("findMember.CreatedBy = " +
//                findMember.getCreatedBy());
//        System.out.println("findMember.LastModifiedBy = " +
//                findMember.getLastModifiedBy());
    }

}