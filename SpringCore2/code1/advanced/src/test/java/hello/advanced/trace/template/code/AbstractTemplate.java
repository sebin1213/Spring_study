package hello.advanced.trace.template.code;

import lombok.extern.slf4j.Slf4j;


// 자식 클래스에 따라서 안에 들어오는 코드가 다름
@Slf4j
public abstract class AbstractTemplate {
    public void execute() {
        long startTime = System.currentTimeMillis();
        //비즈니스 로직 실행
        call(); //상속
        //비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);
    }
    protected abstract void call();
}