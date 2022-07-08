package hello.advanced.trace.threadlocal.code;

import lombok.extern.slf4j.Slf4j;

// threadLocal을 사용해서 동시성 보장 하지만 ThreadLocal.remove를 사용해 쓰레드 로컬에 저장된값을 제거해줘야 한다...!
@Slf4j
public class ThreadLocalService {
    private ThreadLocal<String> nameStore= new ThreadLocal<>();

    public String logic(String name){
        log.info("저장 name={} --> nameStore={}", name,nameStore.get());
        nameStore.set(name);
        sleep(1000);
        log.info("조회 nameStore={}",nameStore.get());
        return nameStore.get();
    }

    private void sleep(int millis){
        try {
            Thread.sleep(millis);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
