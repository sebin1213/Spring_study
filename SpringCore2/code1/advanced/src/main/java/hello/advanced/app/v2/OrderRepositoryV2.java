package hello.advanced.app.v2;

import hello.advanced.trace.TraceId;
import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.hellotrace.HelloTraceV1;
import hello.advanced.trace.hellotrace.HelloTraceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryV2 {
    private final HelloTraceV2 trace;

    public void save(TraceId traceId, String itemId){
        TraceStatus status = null;
        try {
            status = trace.beginSync(traceId,"OrderRepository.save()");
            if(itemId.equals("ex")){
                throw new IllegalStateException("상품아이디가 ex면 예외 터짐");
            }
            sleep(1000);
            trace.end(status); // 예외가 터져도 해당 함수가 호출되어야함...
        }
        catch (Exception e){
            trace.exception(status,e);
            throw e; // 예외를 꼭 다시 던져야한다. 안던지면 위에 로그가 안나옴...
        }
    }

    private void sleep(int millis){
        try{
            Thread.sleep(millis);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}