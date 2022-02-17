package com.example.shop.repository;

import com.example.shop.domain.Member.EmailAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EmailAuthRepository {

    private final EntityManager em;
//
//    public EmailAuth save(EmailAuth emailAuth){
//
//        emailAuth.setExpireDate(LocalDateTime.now().plusMinutes(5L));
//
//        em.persist(emailAuth);
//        return emailAuth;
//    }
//
//    public Optional<EmailAuth> findAuthByEmail(String email, String authToken, LocalDateTime currentTime) {
//        EmailAuth emailAuth = em.find(EmailAuth.class, email);
//        return Optional.ofNullable(emailAuth);
//    }

}
