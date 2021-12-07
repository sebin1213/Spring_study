package jpabook.jpashop.domain;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders") // order는 데이터베이스 order by의 예약어로 잡고있어서 orders로 변경해줌
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //member와 다대일관계
    @JoinColumn(name = "member_id") //매핑을 뭐로할지 외래키(연관관계 주인이니까 order에 써줌)
    private Member member; //주문 회원
}