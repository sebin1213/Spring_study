package Rest.API.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
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

//@WebMvcTest //슬라이스 테스트...웹계층 테스트..? (웹용 bean들만 등록해줌 , reposipory는 빈으로 등록 안함)
@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTests {
    @Autowired
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
                        .accept(MediaTypes.HAL_JSON) // 하이퍼 어플리케이션 랭기지
                        .content(objectMapper.writeValueAsString(event)) // 객체를 json문자열로 변경
                )
                .andDo(print()) //어떤 응답이 오갔는지 확인 (Location 정보에 헤더에 생성된 이벤트를 조횔할수있는 uri가 담겨있다.
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists()) // db에 들어갈때 자동생성된 값으로 나오는지 확인
                .andExpect(header().exists(HttpHeaders.LOCATION)) // 헤더에 LOCATION정보 출력
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE)) // 헤더에 CONTENT_TYPE 정보 출력
                .andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("free").value(false));
    }

    @Test
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

        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

}
