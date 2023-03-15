package Rest.API.events;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {
    @Test
    public void builder(){
        Event event = Event.builder()
                .name("Inflearn Spring rest api")
                .description("pest api development with spring")
                .build();
        Assertions.assertThat(event).isNotNull();
    }

    @Test
    public void javaBean() {
        // Given
        String name = "Event";
        String description = "Spring";

        // When
        Event event = new Event();
        event.setName(name);
        event.setDescription(description);

        // Then
        Assertions.assertThat(event.getName()).isEqualTo(name);
        Assertions.assertThat(event.getDescription()).isEqualTo(description);
    }

    @ParameterizedTest(name = "{index} => basePrice={0}, maxPrice={1}, isFree={2}")
    @MethodSource("testFreeParams")
    public void testFree(int basePrice, int maxPrice, boolean isFree){
        // Given
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();
        //When
        event.update();

        // Then
        Assertions.assertThat(event.isFree()).isEqualTo(isFree);
    }

    // static 있어야 동작
    /*
    private static Object[] testFreeParams() {
        return new Object[]{
                new Object[]{0, 0, true},
                new Object[]{100, 0, false},
                new Object[]{0, 100, false}
        };
    }
    */
    private static Stream<Arguments> testFreeParams() {
        return Stream.of(
                Arguments.of(0, 0, true),
                Arguments.of(100, 0, false),
                Arguments.of(0, 100, false)
        );
    }


    @ParameterizedTest(name = "{index} => location={0}, isOffline={1}")
    @MethodSource("testOfflineParams")
    public void testOffline(String location,boolean isOffline){
        //given
        Event event = Event.builder()
                .location(location)
                .build();
        //when
        event.update();
        //then
        Assertions.assertThat(event.isOffline()).isEqualTo(isOffline);
    }
    private static Stream<Arguments> testOfflineParams() {
        return Stream.of(
                Arguments.of("강남역", true),
                Arguments.of("", false)
        );
    }
}
