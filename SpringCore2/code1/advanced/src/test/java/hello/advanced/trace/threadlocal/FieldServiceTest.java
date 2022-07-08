package hello.advanced.trace.threadlocal;

import hello.advanced.trace.threadlocal.code.FieldService;
import hello.advanced.trace.threadlocal.code.ThreadLocalService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class FieldServiceTest {
    private FieldService fieldService = new FieldService();

    @Test
    void field(){
        log.info("main start");

//        Runnable userA = new Runnable() {
//            @Override
//            public void run() { // 쓰레드를 통해 수행할 내용들을 정의함
//                fieldService.logic("userA");
//            }
//        }

        Runnable userA =() ->{
            fieldService.logic("userA");
        };

        Runnable userB =() ->{
            fieldService.logic("userB");
        };

        Thread threadA = new Thread(userA);
        threadA.setName("thread-A");
        Thread threadB = new Thread(userB);
        threadB.setName("thread-B");

//        /**
//         * 동시성 고려 XXXXX
//         **/
//        threadA.start(); // 쓰레드 a 실행
//        sleep(2000); //2초정도 쉬기 (a가 완전히 끝날때 b실행 동시성 문제 고려 안할때)
//        threadB.start();
//
//        sleep(3000); // 메인 스레드 종료 대기
//        log.info("main exit");


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
