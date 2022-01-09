package jpabook.jpashop;

import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MemberRepository_before {
    @PersistenceContext //엔티티매니저 (jpa할때 필요)
    EntityManager em;

    public Long save(Member member) {
        em.persist(member);
        return member.getId(); // member로 반환하면 되는데 왜 id만 반환..? 커멘드와 쿼리를 분리하라는 원칙에 의해 저장을 하면 커멘드 성이기 때문에 리터값을 잘 안만든가..? 여튼 아이디만 반환하면 나머지 조회 다할수잇어서..?
    }
    public Member find(Long id) {
        return em.find(Member.class, id);
    }
}