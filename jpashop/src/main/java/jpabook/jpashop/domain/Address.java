package jpabook.jpashop.domain;

import javax.persistence.Embeddable;

@Embeddable // 얘는 jpa의 내장타입이기 때문에 이렇게 애너테이션을 붙여줌
public class Address {
    private String city;
    private String street;
    private String zipcode;
}
