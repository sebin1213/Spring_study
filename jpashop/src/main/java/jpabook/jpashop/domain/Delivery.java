package jpabook.jpashop.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Getter @Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @JsonIgnore //무한루프 방지
    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    //EnumType.ORDINAL이면 안됨
    @Enumerated(EnumType.STRING) //ENUM을 사용할려면 어노테이션 추가해야함
    private DeliveryStatus status; //ENUM [READY(준비), COMP(배송)]
}