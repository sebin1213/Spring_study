package hello.proxy.pureproxy.decorater.code;

import lombok.extern.slf4j.Slf4j;

// 이전 출력에 로그나 다른 기능을 추가하고싶을때 이 데코레이터 패턴을 사용함
@Slf4j
public class MessageDecorator implements Component {
    private Component component;
    public MessageDecorator(Component component) {
        this.component = component;
    }
    @Override
    public String operation() {
        log.info("MessageDecorator 실행");
        //중간에 호출하는대상을(realComponent) 저장
        String result = component.operation();
        String decoResult = "*****" + result + "*****";
        log.info("MessageDecorator 꾸미기 적용 전={}, 적용 후={}", result,
                decoResult);
        return decoResult;
    }
}