package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter @Setter
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id") //pk 이름
    private Long id;

    private String name;

    @Embedded //내장타입 사용
    private Address address;

    // member에게 order는 일대다 관계임 (@OneToMany), 반대로 order는 @ManyToOne
    @OneToMany(mappedBy = "member") // 매핑..
    private List<Order> orders = new ArrayList<>();
}











//@Entity
//@Getter @Setter
//public class Member {
//    @Id @GeneratedValue
//    private Long id;
//    private String username;

//    롬복쓰니까 이거 안써도 됨
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//}

