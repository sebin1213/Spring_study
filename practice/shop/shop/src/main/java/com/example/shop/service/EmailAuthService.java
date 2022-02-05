package com.example.shop.service;

import com.example.shop.repository.EmailAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@EnableAsync
public class EmailAuthService {
    private final EmailAuthRepository emailAuthRepository;

    @Async
    public void send(String email, String authToken) {
        SimpleMailMessage smm = new SimpleMailMessage();
        smm.setTo(email+"@gmail.com");
        smm.setSubject("회원가입 이메일 인증");
        smm.setText("http://localhost:8080/sign/confirm-email?email="+email+"&authToken="+authToken);
        javaMailSender.send(smm);
    }
}
