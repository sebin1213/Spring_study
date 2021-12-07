package jpabook.jpashop.domain.item;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("B") // DB입장에서 구분할때 사용함
@Getter @Setter
public class Book extends Item {

    private String author;

    private String isbn;
}