package com.example.shop.controller.form;

import com.example.shop.domain.Address;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter @Setter
public class MemberForm {

    @NotBlank
    @Size(min = 8, message = "아이디는 8자 이상이여야 합니다!")
    private String userid;

    @NotBlank
    @Size(min = 10, message = "비밀번호는 10자 이상이여야 합니다!")
    private String password;

    @NotBlank(message = "회원 이름은 필수 입니다")
    private String username;

    @NotBlank(message = "이메일은 필수 입니다")
    @Email
    private String email;

//    @NotBlank(message = "핸드폰 번호는 필수 입니다")
//    private String phone;
}
