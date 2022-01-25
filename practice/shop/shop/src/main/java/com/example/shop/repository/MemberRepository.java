package com.example.shop.repository;

import com.example.shop.domain.Member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    private final EntityManager em;

    public Member save(Member member){
        em.persist(member);
        return member;
    }
    public Optional<Member> findById(Long id){
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }
    public Optional<Member> findByUserId(String userid){
        Member member = em.find(Member.class, userid);
        return Optional.ofNullable(member);
    }
    public Optional<Member> findByEmail(String email){
        Member member = em.find(Member.class, email);
        return Optional.ofNullable(member);
    }
    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }
}
