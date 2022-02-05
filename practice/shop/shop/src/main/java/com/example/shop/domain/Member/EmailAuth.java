package com.example.shop.domain.Member;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailAuth {

    private static final Long MAX_EXPIRE_TIME = 5L;

    @Id
    @GeneratedValue
    private Long id;

    private String email;
    private String authToken;
    private Boolean expired;
    private LocalDateTime expireDate;

//    @Builder
//    public EmailAuth(String email, String authToken, Boolean expired) {
//        this.email = email;
//        this.authToken = authToken;
//        this.expired = expired;
//        this.expireDate = LocalDateTime.now().plusMinutes(MAX_EXPIRE_TIME); // 만료시간 5분으로 설정함
//    }
//
//    public void useToken() {
//        this.expired = true;
//    }
}