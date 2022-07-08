package hello.proxy.pureproxy.proxy.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CacheProxy implements Subject{

    private Subject target;
    private String cacheValue;

    public CacheProxy(Subject target) {
        this.target = target;
    }

    @Override
    public String operation(){
        log.info("프록시 호출");
        // 한번만 실제 객체를 호출하고 이후에는 저장된 프록시를 호출함
        if(cacheValue == null){
            cacheValue = target.operation();
        }
        return cacheValue;
    }
}
