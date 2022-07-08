package hello.advanced.trace.strategy;

import hello.advanced.trace.strategy.code.strategy.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ContextV2Test {
    /**
     * 전략 패턴 적용
     */
    @Test
    void strategyV1() {
        ContextV2 context = new ContextV2();
        context.execute(new StrategyLogic1());
        context.execute(new StrategyLogic2());
    }

// 익명내부클래스 사용
    @Test
    void strategyV2() {
        ContextV2 context = new ContextV2();
        context.execute(new Strategy() {
            @Override
            public void call() {
                log.info("비즈니스 로직1 실행");
            }
        });
        context.execute(new Strategy() {
            @Override
            public void call() {
                log.info("비즈니스 로직2 실행");
            }
        });
    }
    // 익명내부클래스 좀더더 편하게
    // 인터페이스의 메서드가 한개만 있어서 람다사용가능
    @Test
    void strategyV3() {
        ContextV2 context = new ContextV2();
        context.execute(()->log.info("비즈니스 로직1 실행"));
        context.execute(()->log.info("비즈니스 로직2 실행"));
    }
}
