package study.datajpa.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

//jpa 이벤트로 해결하는 법
@MappedSuperclass // 상속관계가 아니라 속성만 내려쓰는 관계 (데이터만 공유하는것... jpa상속관계 아님)
@Getter
public class JpaBaseEntity {
    @Column(updatable = false) // 이걸 넣으면 실수로 데이터를 변경하더라도 변경되지 않음
    private LocalDateTime createdDate; // 진짜 상속관계가 아님
    private LocalDateTime updatedDate;

    @PrePersist //jpa에서 제공하는 기능 ( persist하기전에 이벤트가 발생)
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdDate = now;
        updatedDate = now; // 여기에 똑같이 데이터를 넣어놔야 나중에 편함 나중에 null처리할때 까다로움
    }
    @PreUpdate //업데이트 하기전에 이벤트 발생
    public void preUpdate() {
        updatedDate = LocalDateTime.now();
    }

}