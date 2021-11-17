// DAO
package start.hello_spring.domain;

import javax.persistence.*;

@Entity // jpa가 관리하는 엔티티로 설정
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) // IDENTITY DB에 아이디 추가하면 자동으로 증가하는거
    // 요구사항에서 아이디 식별자와 이름이 있다고 정의했음
    // id는 임의의 값 시스템이 정해줌
    private Long id;

//    @Column(name = "username") 이렇게하면 db username 컬럼과 매핑됨
    private String name;
    // getter setter
    public Long getId() {

        return id;
    }
    public void setId(Long id) {

        this.id = id;
    }
    public String getName() {

        return name;
    }
    public void setName(String name) {

        this.name = name;
    }
}