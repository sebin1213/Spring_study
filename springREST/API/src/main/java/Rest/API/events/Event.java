package Rest.API.events;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@EqualsAndHashCode(of = "id") // 연관관계에 해당하는 필드를 넣으면 안됨..(스택오버플로우 발생)
@Entity
public class Event {

    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location; // (optional) 이게 없으면 온라인 모임
    private int basePrice; // (optional)
    private int maxPrice; // (optional)
    private int limitOfEnrollment;
    private boolean offline;
    private boolean free;

    @Builder.Default
    @Enumerated(EnumType.STRING) // 기본값이 EnumType.ORDINAL 임 ORDINAL은 enum 값들에 0,1,2 이렇게 순서를 붙여줌 만약 이 데이터의 순서가 바뀌면 꼬일수가있어 SPRING으로 저장
    private EventStatus eventStatus = EventStatus.DRAFT;

//    @ManyToOne
//    @JsonSerialize(using = AccountSerializer.class)
//    private Account manager;
//
    public void update() {
        // Update free
        if (this.basePrice == 0 && this.maxPrice == 0) {
            this.free = true;
        } else {
            this.free = false;
        }
        // Update offline
        if (this.location == null || this.location.isBlank()) {
            this.offline = false;
        } else {
            this.offline = true;
        }
    }
}
