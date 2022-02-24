package hello.servlet.domain.member;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Member {

    private Long id;
    private String username;
    private int age;

    public Member() {
        // 기본 생성자는 JPA를 사용할 때 디폴트 생성자가 반드시 존재해야해서 작성하신 습관
        //혹은
        //기본 생성자가 필요할 경우를 대비해서 만들어놨다고함
    }

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }
}
