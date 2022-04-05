package study.datajpa.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

//보통 등록수정일은 모두 필요함 근데 등록자 수정자 는 필요없는 경우도 있음 그래서 따로 만들어서 상속받아 사용
@EntityListeners(AuditingEntityListener.class) // 이벤트로 동작한다....????
@MappedSuperclass
@Getter
public class BaseTimeEntity {
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
}