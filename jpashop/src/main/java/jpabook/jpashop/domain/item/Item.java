package jpabook.jpashop.domain.item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.domain.Category;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) //싱글테이블 전략을 사용
@DiscriminatorColumn(name = "dtype")
@Getter @Setter// @Setter 이 안에서 수정할꺼라 필요없음
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int price;

    private int stockQuantity; // 재고수량이 item안에 있음 그러면 이 데이터를 가지고 있는곳에서 비즈니스 로직을 만드는게 가장 응집도가 있음

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<Category>();


    //===비즈니스 로직 ===//
    /**
     * 재고 증가
     **/
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     * 재고 감가
     **/

    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) { // 남은 수량이 0보다 작을때
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
