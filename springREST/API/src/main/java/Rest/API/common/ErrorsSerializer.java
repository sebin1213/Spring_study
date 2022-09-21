package Rest.API.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;

import java.io.IOException;

/**
 * https://docs.microsoft.com/ko-kr/dotnet/api/system.text.json.utf8jsonwriter.writestartarray?view=net-6.0
 * 참고
 *
 * https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/validation/FieldError.html
 *
 * **/

@JsonComponent // 이렇게 등록을 한다면 objectMapper는 이거를 사용해서 Errors를 json으로 시리얼라이징할때 이거를 사용함
public class ErrorsSerializer extends JsonSerializer<Errors> { // JsonSerializer<Errors> Errors타입을 변환하는 json 시리얼라이저

    @Override
    public void serialize(Errors errors, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        // writeStartArray()를 통해 JSON 배열의 시작 부분을 씁니다.
        gen.writeStartArray(); // errors에서는 에러가 여러개 존재할수있으니 배열로 담아주기위해 startArray, endArray를 사용함

        // getFieldErrors : EventValidator에서 errors.rejectValue 할 때 필드에러로 들어가는데 그때 그 필드에러를 가져옴
        // getGlobalErrors() : EventValidator에서 errors.reject 이렇게하면 글로벌에러로 들어감
        errors.getFieldErrors().forEach(e -> {
            try {
                gen.writeStartObject();                                                         // JSON 개체의 시작 부분을 씁니다.
                gen.writeStringField("field", e.getField());                           // 문자열로 지정한 속성 이름 및 문자열 텍스트 값(JSON 문자열)을 JSON 개체의 이름/값 쌍의 일부로 작성합니다.
                gen.writeStringField("objectName", e.getObjectName());
                gen.writeStringField("code", e.getCode());
                gen.writeStringField("defaultMessage", e.getDefaultMessage());
                Object rejectedValue = e.getRejectedValue();  // // 에러가 발생한 값이 존재한다면 rejectedValue에 넣어준다.
                if (rejectedValue != null) {
                    gen.writeStringField("rejectedValue", rejectedValue.toString());
                }
                gen.writeEndObject();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        errors.getGlobalErrors().forEach(e -> {
            try {
                gen.writeStartObject();
                gen.writeStringField("objectName", e.getObjectName());
                gen.writeStringField("code", e.getCode());
                gen.writeStringField("defaultMessage", e.getDefaultMessage());
                gen.writeEndObject();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        gen.writeEndArray();
    }
}