package Rest.API.events;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;
import java.time.LocalDateTime;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE) // 모든 핸들러는 HAL_JSON라는 content 타입으로 응답을 보냄
public class EventController {
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
    public final EventRepository eventRepository;

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
    public ResponseEntity createEvent(@RequestBody EventDto eventDto){

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
        return ResponseEntity.created(createUri).body(event);
    }

}
