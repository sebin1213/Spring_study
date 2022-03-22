package study.datajpa.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

//@Entity
//@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//public class Item {
////    @Id @GeneratedValue
////    private Long id;
//    @Id
//    private String id; // 만약 내가 pk를 직접설정한다면 save는 어케 동작할지
////    protected Item(){}
//    public Item(String id){
//        this.id = id;
//    }
//}
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item implements Persistable<String> {
    @Id
    private String id; // 만약 내가 pk를 직접설정한다면 save는 어케 동작할지

    @CreatedDate
    private LocalDateTime createdDate;

    public Item(String id){
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    // @CreatedDate는 persist 되기전에 호출됨 그니까 해당 객체가 null이면 새로운객체라 판단
    @Override
    public boolean isNew() {
        return createdDate == null;
    }
}
