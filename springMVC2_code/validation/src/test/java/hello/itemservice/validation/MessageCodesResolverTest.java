package hello.itemservice.validation;

import org.junit.jupiter.api.Test;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.FieldError;
import org.springframework.validation.MessageCodesResolver;

import static org.assertj.core.api.Assertions.*;

public class MessageCodesResolverTest {

//    MessageCodesResolver 검증오류 코드 (메세지 코드를 생성)
//    메시지 코드를 만들 때 required 처럼 단순하게 만들수도 있고 required.item.price 처럼 자세히 만들수도 있다. 단순하게 만들면 범용성이 좋아서 여러곳에서 사용할 수 있지만 세밀한 오류 메시지 작성이 불가능하다. 반대로, 자세하게 만들면 세밀한 메시지 작성이 가능하지만 범용성이 떨어진다. 가장 좋은 방법은, 범용성으로 사용하다가, 세밀하게 작성해야 하는 경우에 세밀한 내용이 적용되도록 오류 코드를 단계적으로 사용하는 것이다.
//
//    스프링은 MessageCodesResolver 를 통해 메시지 코드를 단계적으로 사용할 수 있도록 도와준다. 이 인터페이스 덕분에 범용적인 메시지와 세밀한 메시지 모두 단순히 추가할 수 있게하여 매우 편리하게 메시지를 관리할 수 있다.

    MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();

    @Test // 오브젝트를 넣었을때 해당하는 오류 메세지가 잘 출력되는지 확인
    void messageCodesResolverObject() {
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item");
        for (String messageCode : messageCodes){
            System.out.println("messageCode = " + messageCode);
        }
        assertThat(messageCodes).containsExactly("required.item", "required");
    }

    @Test // 오브젝트의 필드까지 넣었을때
    void messageCodesResolverField() {
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item", "itemName", String.class);

        for (String messageCode : messageCodes){
            System.out.println("messageCode = " + messageCode);
        } // 여기에서 탐색되는 순서대로 error에서 찾아옴

        assertThat(messageCodes).containsExactly(
                "required.item.itemName",
                "required.itemName",
                "required.java.lang.String",
                "required"
        );
    }

}
