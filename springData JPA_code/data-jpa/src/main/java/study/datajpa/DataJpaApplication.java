package study.datajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;
import java.util.UUID;

@EnableJpaAuditing
@SpringBootApplication
public class DataJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataJpaApplication.class, args);
	}
	@Bean
	public AuditorAware<String> auditorProvider() {
		return () -> Optional.of(UUID.randomUUID().toString()); // 랜덤아이디 생성
	}

	// 위 람다를 풀어서 쓰면 이렇게 작성함
	//AuditorAware 에 있는 getCurrentAuditor() 를 구현한 것
//	@Bean
//	public AuditorAware<String> auditorProvider() {
//	return new AuditorAware<String>(){ // 인터페이스에서 메서드가 하나면 람다로 변경가능
//		@Override
//				public  Optional<String> getCurrentAuditor(){
//			return Optional.of(UUID.randomUUID().toString());
//		}
//	}
	// 실제로 스프링 시큐리티를 사용하면 폴더나..홀더나...??..? 세션의 아이디를 꺼내야함


	}
