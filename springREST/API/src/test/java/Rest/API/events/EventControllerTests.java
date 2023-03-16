package Rest.API.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@WebMvcTest //슬라이스 테스트...웹계층 테스트..? (웹용 bean들만 등록해줌 , reposipory는 빈으로 등록 안함 - 하지만 단위테스트라 보기엔 어려움... dispatcher 서블릿이나 여러 매퍼..컨버터 등등 조합된 상태ㅑ로 진행되는 테스트라)
@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTests {
    @Autowired // 웹 서버를 띄우지 않고도 스프링 MVC (DispatcherServlet)가 요청을 처리하는 과정을 확인할 수 있어 컨트롤러 테스트용으로 자주 쓰임.
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

//    @MockBean // repository는 웹용 빈이 아님 때문에 WebMvcTest 에서 빈으로 등록하지 못해 불러올수 없음, 또한 mock 객체이기 때문에 reture되는 값이 모두 null 값임
//    EventRepository eventRepository;

    //    @Test
//    public void createEvent() throws Exception {
//
//        mockMvc.perform(post("/api/events/")
//                        .contentType(MediaType.APPLICATION_JSON) // 브라우저에 요청을 보내면 브라우저가 기본으로 요청 헤더에 contenttype을 넣어줌 하지만 테스트의 경우 직접 명시하는 것이 좋음
//                        .accept(MediaTypes.HAL_JSON) // 하이퍼 어플리케이션 랭기지
//                )
//                .andDo(print()) //어떤 응답이 오갔는지 확인 (Location 정보에 헤더에 생성된 이벤트를 조횔할수있는 uri가 담겨있다.
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("id").exists()); // db에 들어갈때 자동생성된 값으로 나오는지 확인
//    }

//    @Test
//    public void createEvent() throws Exception {
//        Event event = Event.builder()
//                .id(100)
//                .name("spring")
//                .description("rest api development with spring")
//                .beginEnrollmentDateTime(LocalDateTime.of(2022,9,1,14,33))
//                .closeEnrollmentDateTime(LocalDateTime.of(2022,9,2,14,33))
//                .beginEventDateTime(LocalDateTime.of(2022,9,3,14,33))
//                .endEventDateTime(LocalDateTime.of(2022,9,4,14,33))
//                .basePrice(100)
//                .maxPrice(200)
//                .limitOfEnrollment(100)
//                .location("hhh")
//                .free(true)
//                .build();
//        // .contentType(MediaType.APPLICATION_JSON) json타입으로 넘겨준다 했으니 위의 객체를 json타입으로 변경해야한다.
//
//        event.setId(10);
//        Mockito.when(eventRepository.save(event)).thenReturn(event); // 메서드 호출 조건 -> 조건 만족시 리턴값
//        /** mocking 적용안됨 **/
//        // 새로 builder를 이용해 또다른 event 객체를 만들어서 위 객체와 다름
//
//        mockMvc.perform(post("/api/events/")
//                        .contentType(MediaType.APPLICATION_JSON) // 브라우저에 요청을 보내면 브라우저가 기본으로 요청 헤더에 contenttype을 넣어줌 하지만 테스트의 경우 직접 명시하는 것이 좋음
//                        .accept(MediaTypes.HAL_JSON) // 하이퍼 어플리케이션 랭기지
//                        .content(objectMapper.writeValueAsString(event)) // 객체를 json문자열로 변경
//                )
//                .andDo(print()) //어떤 응답이 오갔는지 확인 (Location 정보에 헤더에 생성된 이벤트를 조횔할수있는 uri가 담겨있다.
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("id").exists()) // db에 들어갈때 자동생성된 값으로 나오는지 확인
//                .andExpect(header().exists(HttpHeaders.LOCATION)) // 헤더에 LOCATION정보 출력
//                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE)); // 헤더에 CONTENT_TYPE 정보 출력
//    }

    @Test
    @DisplayName("옳은 값이 들어온 테스트")
    public void createEvent() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("spring")
                .description("rest api development with spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2022,9,1,14,33))
                .closeEnrollmentDateTime(LocalDateTime.of(2022,9,2,14,33))
                .beginEventDateTime(LocalDateTime.of(2022,9,3,14,33))
                .endEventDateTime(LocalDateTime.of(2022,9,4,14,33))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("hhh")
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        mockMvc.perform(post("/api/events/")
                        .contentType(MediaType.APPLICATION_JSON) // 브라우저에 요청을 보내면 브라우저가 기본으로 요청 헤더에 contenttype을 넣어줌 하지만 테스트의 경우 직접 명시하는 것이 좋음
                        .accept(MediaTypes.HAL_JSON) // 어떤 응답을 받고싶은지, 하이퍼 어플리케이션 랭기지
                        .content(objectMapper.writeValueAsString(event)) // 객체를 json문자열로 변경
                )   // perform ..요청
                .andDo(print()) //어떤 응답이 오갔는지 확인 (Location 정보에 헤더에 생성된 이벤트를 조횔할수있는 uri가 담겨있다.
                .andExpect(status().isCreated())    // 201 상태인지 확인
                .andExpect(jsonPath("id").exists()) // db에 들어갈때 자동생성된 값으로 나오는지 확인
                .andExpect(header().exists(HttpHeaders.LOCATION)) // 헤더에 LOCATION정보 출력
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE)) // 헤더에 CONTENT_TYPE 정보 출력
                .andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("free").value(false));

    }

//    @Test
    @DisplayName("properties 옳지않은 값이 들어왔을때 에러가 발생하는 테스트") // properties에 설정 추가 spring.jackson.deserialization.fail-on-unknown-properties=true 모르는 프로퍼티가 넘어가면 fail
    public void createEvent_Bad_Request() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 23, 14, 21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 24, 14, 21))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 25, 14, 21))
                .endEventDateTime(LocalDateTime.of(2018, 11, 26, 14, 21))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        // 모르는 필드가(dto니까 간소화시켜놓음) 넘어가면 BadRequest
        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @DisplayName("빈값을 입력했을때 에러가 발생하는 테스트") // vaild, bindingResult 설정해둠
    void createEvent_Bad_Request_Empty_Input() throws Exception{
        EventDto eventDto = EventDto.builder().build();

        this.mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventDto))
                )
                .andExpect(status().isBadRequest());
    }

    @Test // 값이 들어오지만 잘못된 값일때
    @DisplayName("입력값을 잘못입력했을때 에러가 발생하는 테스트")
    void createEvent_Bad_Request_Wrong_Input() throws Exception{
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 23, 14, 21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 12, 24, 14, 21)) // 월 잘못입력
                .beginEventDateTime(LocalDateTime.of(2018, 11, 29, 14, 21))
                .endEventDateTime(LocalDateTime.of(2018, 11, 26, 14, 21)) // 끝나는 날이 시작일보다 빠름
                .basePrice(100000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .build();

        this.mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventDto))
                )
                .andExpect(status().isBadRequest()) //아래와 같은 에러메세지를 가져와야함 에러메세지를 가져오기위해서는 Error객체를 따로 시리얼라이징해야함(java bean 규약에 안맞아서 objectmapper가 자동적으로 못함못함)
                .andDo(print())
                .andExpect(jsonPath("$[0].objectName").exists())      // 에러의 배열중에 object이름과
                .andExpect(jsonPath("$[0].field").exists())           // 어떤필드에서 발생한 에러인지()
                .andExpect(jsonPath("$[0].defaultMessage").exists())  // 에러 기본메세지
                .andExpect(jsonPath("$[0].code").exists())            // 에러 코드는 어떤것이였는지
                .andExpect(jsonPath("$[0].rejectedValue").exists())   // 에러가 발생한 값은 무엇이였는지
        ;

    }

}
