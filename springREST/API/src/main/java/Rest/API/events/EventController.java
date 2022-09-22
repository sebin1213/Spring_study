package Rest.API.events;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE) // 모든 핸들러는 HAL_JSON라는 content 타입으로 응답을 보냄
public class EventController {
    public final EventRepository eventRepository;
    public final EventValidator eventValidator;

//    @PostMapping("/api/events")
//    public ResponseEntity createEvent(){
//        URI createUri = linkTo(methodOn(EventController.class).createEvent()).slash("{id}").toUri(); // 링크를 만들고 uri로 변경
//        return ResponseEntity.created(createUri).build();
//    }


//    //     객체를 body에도 담고 헤더정보 등을 세팅할수 있도록 하기위해 ResponseEntity로 반환
//    @PostMapping
//    public ResponseEntity createEvent(@RequestBody Event event){
//        URI createUri = linkTo(EventController.class).slash("{id}").toUri(); // 링크를 만들고 uri로 변경
//        event.setId(10);
//        return ResponseEntity.created(createUri).body(event);
//        // linkTo 링크를 만듬
//    }

//    @PostMapping
//    public ResponseEntity createEvent(@RequestBody Event event){
//        Event newEvent = this.eventRepository.save(event); // repository 저장
//        URI createUri = linkTo(EventController.class).slash(newEvent.getId()).toUri(); // 링크를 만들고 uri로 변경
//        event.setId(10);
//        return ResponseEntity.created(createUri).body(event);
//    }

    /** 
     * 이전에는 DTO를 엔티티로 변환할라면 builder를 또 이용하거나 정적팩토리 메서드를 이용했음
     * modelmapper이용해 변환..? 하지만 속도는 더 느림 (별로같음)
     * **/
    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors){
        if (errors.hasErrors()){  // 바인딩할때 검증과 관련된 오류가 발생하면 동작
            System.out.println("error............");
            // return ResponseEntity.badRequest().body(errors);
            // 에러메세지를 전송하기위해 body에 error메세지를 담아 보내자..!
            // 하지만 불가능함... body에 담으면 error객체를 json으로 변경할수 없어서 불가능
            // 왜?? 이벤트객체는 자바 bean스펙에 준수한 객체이기 때문에 objectmapper는 이 객체를 json형태로 기본 시리얼라이저인 bean시리얼라이저를 사용해 변환한다.
            // 하지만 errors는 자바 bean스펙에 준수한 객체가 아님... 때문에 objectmapper를 이용해 json형태로 변경하고싶어도 bean시리얼라이저를 사용할때 에러가 발생해 변환할 수 없다.
            // 왜 json으로 변환..? 맨위에 produces = MediaTypes.HAL_JSON_VALUE json형태로 보낼꺼라고 명시함

            return ResponseEntity.badRequest().build();
        }

        eventValidator.validate(eventDto,errors);
        if (errors.hasErrors()){  // 바인딩 외에 발생하면 잘못입력된 데이터가 존재하면 동작
            return ResponseEntity.badRequest().build();
        }

        Event event = Event.builder()
                .name(eventDto.getName())
                .description(eventDto.getDescription())
                .beginEnrollmentDateTime(eventDto.getBeginEnrollmentDateTime())
                .closeEnrollmentDateTime(eventDto.getCloseEnrollmentDateTime())
                .beginEventDateTime(eventDto.getBeginEventDateTime())
                .endEventDateTime(eventDto.getEndEventDateTime())
                .location(eventDto.getLocation())
                .basePrice(eventDto.getBasePrice())
                .maxPrice(eventDto.getMaxPrice())
                .limitOfEnrollment(eventDto.getLimitOfEnrollment())
                .build();

        Event newEvent = eventRepository.save(event); // repository 저장
        URI createUri = linkTo(EventController.class).slash(newEvent.getId()).toUri(); // 링크를 만들고 uri로 변경
        return ResponseEntity.created(createUri).body(event); // created 응답을 보낼때는 항상 uri가 존재해야함
    }

}
