package start.hello_spring.repository;

import start.hello_spring.domain.Member;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class JpaMemberRepository implements MemberRepository{
    private final EntityManager em;
    public JpaMemberRepository(EntityManager em) {
        this.em = em;
    }
    public Member save(Member member) {
        em.persist(member); //jpa가 인서트 쿼리 만들어서 아이디까지 다 집어넣어줌
        return member;
    }
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id); // 식별자 pk랑 find쓰면 그냥 찾아짐
        return Optional.ofNullable(member); // 혹시 값이 없을수도있으니까
    }
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();

    }
    public Optional<Member> findByName(String name) {
        List<Member> result = em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
        return result.stream().findAny();
    }
}
