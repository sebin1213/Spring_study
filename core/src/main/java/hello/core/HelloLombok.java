package hello.core;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString // ToString 만들어줌 soutv 하면 끝
public class HelloLombok {
    private String name;
    private int age;

    public static void main(String[] args){
        HelloLombok helloLombok = new HelloLombok();
        helloLombok.setName("asda");

//        String name = helloLombok.getName();
//        System.out.println("name = "+ name);
        System.out.println("helloLombok = " + helloLombok);
    }
}
