package com.example.shop;

import com.example.shop.domain.Member.Member;
import com.example.shop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

    // 스프링 라이프사이클이 있어서 트랜젝션 먹이고 이런게 잘 안됨 그래서 별도의 빈으로 등록해야함
    @PostConstruct//애플리케이션 로딩시점에 이거를 호출해주고싶어서
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }
    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;
        public void dbInit1() {
            Member member = createMember("1213hhh", "1122pas", "sebin", "1213hop@mansld.dod");
            em.persist(member);
        }
        public void dbInit2() {
            Member member = createMember("aasdasahh", "222", "sebi222n", "adasda@dd");
            em.persist(member);
        }
        private Member createMember(String userid, String password, String username,
                                    String email) {
            Member member = new Member();
            member.setUserid(userid);
            member.setPassword(password);
            member.setUsername(username);
            member.setEmail(email);
            return member;
        }
    }
}