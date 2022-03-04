package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryOld {
    private  final EntityManager em;


//    @PersistenceContext // 엔티티 매니저를 주입받을 수 있음
//    private EntityManager em;

    public void save(Member member){
        em.persist(member);
    }

    public Member findOne(Long id) {    // find는 기본 키로 엔티티를 조회할때만 사용

        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name",
                        Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
