package hello.advanced.trace.threadlocal;

import hello.advanced.trace.threadlocal.code.FieldService;
import hello.advanced.trace.threadlocal.code.ThreadLocalService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
// 이전과 달리 nameStore의 값이 null값으로 나타남
// thread의 지역변수 개념인 threadLocal을 사용했기 때문에
@Slf4j
public class ThreadLocalServiceTest {
    private ThreadLocalService service = new ThreadLocalService();

    @Test
    void field(){
        log.info("main start");

        Runnable userA =() ->{
            service.logic("userA");
        };

        Runnable userB =() ->{
            service.logic("userB");
        };

        Thread threadA = new Thread(userA);
        threadA.setName("thread-A");
        Thread threadB = new Thread(userB);
        threadB.setName("thread-B");


        threadA.start(); // 쓰레드 a 실행
        sleep(100); //2초정도 쉬기 (a가 완전히 끝날때 b실행 동시성 문제 고려 안할때)
        threadB.start();

        sleep(3000); // 메인 스레드 종료 대기
        log.info("main exit");
    }

    private void sleep(int millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
