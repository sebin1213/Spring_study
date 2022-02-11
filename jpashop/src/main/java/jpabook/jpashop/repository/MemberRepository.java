package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
// 스프링 데이터는 JpaRepository 여기에 save, findById, saveall 등등 겁나 많음!
// 구현체 만들필요없이 알아서 해줌
public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByName(String name);
}