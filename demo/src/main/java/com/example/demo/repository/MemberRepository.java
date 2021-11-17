// 회원 객체를 저장하는 저장소를 만듬
package com.example.demo.repository;
import com.example.demo.domain.Member;

import java.util.List;
import java.util.Optional;

// 인터페이스
public interface MemberRepository {
    Member save(Member member); //회원을 저장
    Optional<Member> findById(Long id);// id로 회원을 찾을꺼임
    Optional<Member> findByName(String name);
    List<Member> findAll(); // 지금까지 저장된 회원리스트 모두 반환
}