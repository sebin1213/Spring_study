package com.example.shop.domain.Member;

import com.example.shop.domain.Address;
import com.example.shop.domain.DeliveryStatus;
import com.example.shop.domain.Order;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import static com.example.shop.domain.Member.AuthorityStatus.USER;

@Entity
//@DynamicInsert //하이버네이트는 insert나 update 시 모든 컬럼을 사용하지만 default값을 넣어주기 위해 null인 필드를 제외하는 어노테이션을 추가한다.
//@DynamicUpdate //update 시 null 인필드 제외
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

    private int point = 0;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();

    @Embedded
    private Address address;

    //EnumType.ORDINAL이면 안됨
    @Enumerated(EnumType.STRING) //ENUM을 사용할려면 어노테이션 추가해야함
    private DeliveryStatus status; //ENUM [READY(준비), COMP(배송)]

    @JsonIgnore
    private AuthorityStatus authority = USER;


    private String refreshToken;

    private Boolean emailAuth;

}
