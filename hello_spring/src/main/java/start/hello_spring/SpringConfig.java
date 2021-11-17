package start.hello_spring;
import org.springframework.beans.factory.annotation.Autowired;
import start.hello_spring.aop.TimeTraceAop;
import start.hello_spring.repository.*;
import start.hello_spring.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

// 자바 코드로 직접 스프링 빈 등록하기
// 왜?? 우리 db바꿀껀데 여러개 손대서 바꿀필요 줄일라고

@Configuration
public class SpringConfig {

    private final MemberRepository memberRepository; //스프링 데이터 JPA 회원 리포지토리를 사용하도록 스프링 설정 변경
    public SpringConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

//    private final DataSource dataSource;
//
//    private final EntityManager em; //엔티티매니저를 가져와야함
//
//    @Autowired
//    public SpringConfig(DataSource dataSource, EntityManager em) {
//        this.dataSource = dataSource;
//        this.em = em;
//    }

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository);
    }

//    @Bean // aop 스프링 빈에 직접 등록 ( aop 쓰이는지 확인하기위해 이렇게 하는게 더 좋을듯)
//    public TimeTraceAop timeTraceAop() {
//        return new TimeTraceAop();
//    }

    //    @Bean
//    public MemberRepository memberRepository() {
//        return new MemoryMemberRepository();
//        return new JdbcMemberRepository(dataSource); // Jdbc 이렇게 바꾸면 다른 코드 손안대고 수정할수 있겠져?
//        return new JdbcTemplateMemberRepository(dataSource); // JdbcTemplate 버전
//        return new JpaMemberRepository(em);
//    }
}