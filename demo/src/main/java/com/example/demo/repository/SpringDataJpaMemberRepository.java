// 스프링 데이터 JPA 회원 리포지토리
package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.domain.Member;

import java.util.Optional;

public interface SpringDataJpaMemberRepository extends JpaRepository<Member,
        Long>, MemberRepository {
    Optional<Member> findByName(String name);
}