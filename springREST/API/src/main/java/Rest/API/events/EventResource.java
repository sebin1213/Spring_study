package Rest.API.events;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.util.Arrays;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

//@Getter
//@AllArgsConstructor
public class EventResource extends EntityModel<Event> { //event를 event 리소스로 변경
//    @JsonUnwrapped // EventResource를 시리얼라이징하면 객체로 존재하는 필드는 event : {id:~~~} 이런형식으로 나오는데 객체내부의 필드값만 출력하고싶을때 해당어노테이션 사용
//    (하지만 EntityModel<Event> 사용하면 자동적으로 언랩해줌)
//    private Event event;

    public EventResource(Event event, Link... links) {
        super(event, Arrays.asList(links));
        //매번 self 링크를 만들어줘야 하니 생성자에 넣었다.
        add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
    }
}
