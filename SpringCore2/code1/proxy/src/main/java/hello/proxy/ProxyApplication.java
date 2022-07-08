package hello.proxy;

import hello.proxy.config.AppV1Config;
import hello.proxy.config.AppV2Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import({AppV1Config.class, AppV2Config.class}) // config import 클래스를 spring bean으로 등록 ( config가 스프링 빈으로 등록이 되야 나머지들이 등록됨)
@SpringBootApplication(scanBasePackages = "hello.proxy.app") //주의 (component scan 을 해당 파일 아래있는거만 함, 시작위치 지정) 굳이 시작위치 지정한 이유는 위에 import할때 config 버전별로 넣어줄라고
public class ProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProxyApplication.class, args);
	}

}
