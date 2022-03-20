package study.datajpa.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class) // 이벤트로 동작한다....????
@MappedSuperclass
@Getter
public class BaseEntity extends BaseTimeEntity {
//    @CreatedDate
//    @Column(updatable = false)
//    private LocalDateTime createdDate;
//    @LastModifiedDate
//    private LocalDateTime lastModifiedDate;

    // 시간이라는건 현재 시간을 넣으면 되는데 등록자라는건 어디에서 가져와서 등록을 할까....??
    //jpa애플리케이션에서 확인
    @CreatedBy
    @Column(updatable = false)
    private String createdBy;
    @LastModifiedBy
    private String lastModifiedBy;
}
