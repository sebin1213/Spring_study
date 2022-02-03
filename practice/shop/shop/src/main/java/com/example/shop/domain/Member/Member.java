package com.example.shop.domain.Member;

import com.example.shop.domain.Address;
import com.example.shop.domain.DeliveryStatus;
import com.example.shop.domain.Order;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@JsonIgnoreProperties(value={"password"})
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true, length = 30)
    private String userid;

    @JsonIgnore
    private String password;

    @Column(length = 50)
    private String username;

    @Column(unique = true)
    private String email;

//    @Column(unique = true)
    private String phone;
    private String point;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();

    @Embedded
    private Address address;

    //EnumType.ORDINAL이면 안됨
    @Enumerated(EnumType.STRING) //ENUM을 사용할려면 어노테이션 추가해야함
    private DeliveryStatus status; //ENUM [READY(준비), COMP(배송)]

    @JsonIgnore
    private AuthorityStatus authority;

    @JsonIgnore
    private VerifyStatus verify;

}
