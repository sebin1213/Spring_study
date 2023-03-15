package Rest.API.events;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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
            // 하지만 불가능함... body에 담으면 error객체를 json으로 변경할수 없어서 불가능(json으로 변환하느 이유는 produces = MediaTypes.HAL_JSON_VALUE라고 명시했기때문)
            // 왜?? 내가 선언한 Event 객체는 자바 bean스펙에 준수한 객체이기 때문에 objectmapper는 이 객체를 json형태로 기본 시리얼라이저인 bean시리얼라이저를 사용해 변환한다.
            // 하지만 errors는 자바 bean스펙을 준수하고있는 객체가 아님... 때문에 objectmapper를 이용해 json형태로 변경하고싶어도 bean시리얼라이저를 사용할때 에러가 발생해 변환할 수 없다.
            // 때문에 ErrorsSerializer 라는 시리얼라이저를 만들어 Errors를 json으로 시리얼라이징할때 해당 클래스를 사용할 수 있도록 함
            return ResponseEntity.badRequest().body(errors);
        }

        eventValidator.validate(eventDto,errors);
        if (errors.hasErrors()){  // 바인딩 외에 발생하면 잘못입력된 데이터가 존재하면 동작
            return ResponseEntity.badRequest().body(errors);
        }

        Event event = eventDto.toEntity();
        event.update();
        Event newEvent = eventRepository.save(event); // repository 저장
        WebMvcLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(newEvent.getId());
        URI createUri = linkTo(EventController.class).slash(newEvent.getId()).toUri(); // 링크를 만들고 uri로 변경
        EventResource eventResource = new EventResource(event);
        eventResource.add(linkTo(EventController.class).withRel("query-events"));
        eventResource.add(selfLinkBuilder.withSelfRel()); //self랑 update랑 같은링크인데 기능만 다름
        eventResource.add(selfLinkBuilder.withRel("update-event")); // 링크가 같아도 put메서드를 사용하기때문에 상관없음

//        EntityModel<Event> eventEntityModel = EntityModel.of(event,
//                selfLinkBuilder.slash(event.getId()).withSelfRel(),
//                linkTo(EventController.class).withRel("query-events"),
//                selfLinkBuilder.withRel("update-event")
//        ); // 따로 클래스를 작성하지않고 이렇게 작성해도 됨

        return ResponseEntity.created(createUri).body(eventResource); // created 응답을 보낼때는 항상 uri가 존재해야함
    }

}
