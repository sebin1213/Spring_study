## 스프링 컨테이너

스프링에서 사용할 객체들을 담고있는 것.
등록된 스프링 빈을 생성하고 의존관계를 주입하고 생명주기를 관리해준다.

**AppConfig**

```java
@Configuration
public class AppConfig {
    
    @Bean 
    public MemberService() {
        return new MemberServiceImpl(memberRepository());
    }
    
    @Bean
    public OrderService orderService() {
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }
    
    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }
    
    @Bean
    public DsicountPolicy discountPolicy() {
        return new RateDiscountPolicy();
    }
}
```

- AppConfig에 설정을 구성한다는 뜻의 `@Configuration`을 붙여준다.
- 각 메서드에 `@Bean`을 붙여준다. 이렇게 하면 스프링 컨테이너에 스프링 빈으로 등록한다.

**애플리케이션 실행하는 부분**

```java
public class SpringApplication {

    public static void main(String[] args) {
    
//        AppConfig appConfig = new AppConfig();
//        MemberService memberService = appConfig.memberService();

        /**
         * 스프링은 애플리케이션 컨텍스트로 시작, 얘가 모든 것을 관리
         * 이게 스프링 컨테이너라고 생각하면 됨
         * 인자로 전달해준 설정 정보를 바탕으로 빈을 다 등록해서 관리해줌
         */
        ApplicationContext appConfig = new AnnotationConfigApplicationContext(AppConfig.class);
        
        MemberService memberService = applicationContext.getBean("memberService", MemberService.class);
        OrderService orderService = applicationContext.getBean(AppConfig.class);
        
        long memberId = 1L;
        Member member = new Member(memberId, "memberA", Grade.VIP);
        memberService.join(member);
        
        Order order = orderService.createOrder(memberId, "itemA", 1000);
    }
}
```

- `ApplicationContext`를 스프링 컨테이너라고 한다.
- 기존에는 개발자가 AppConfig를 사용해서 직접 객체를 생성하고 DI를 했지만, 이제부터는 스프링 컨테이너를 통해서 사용한다.
- 스프링 컨테이너는 `@Configuration`이 붙은 AppConfig를 설정(구성)정보로 사용한다.
- 여기서 `@Bean`이 붙은 메서드를 모두 호출해서 반환된 객체를 스프링 컨테이너에 등록한다. 이렇게 스프링 컨테이너에 등록된 객체를 **스프링 빈**이라고 한다.
- 스프링 빈은 `@Bean`이 붙은 메서드의 이름을 스프링 빈의 이름으로 사용한다. (memberService, orderService)
- 이전에는 개발자가 필요한 객체를 AppConfig를 사용해서 **직접 조회**했지만, 이제부터는 스프링 컨테이너를 통해서 필요한 스프링 빈(객체)를 찾아야한다.
- 스프링 빈은 `applicationContext.getBean()`메서드를 사용해서 찾을 수 있다.
- 기존에는 개발자가 직접 자바코드로 모든 것을 했다면 이제부터는 스프링 컨테이너에 객체를 스프링 빈으로 등록하고, 스프링 컨테이너에서 스프링 빈을 찾아서 사용하도록 변경되었다.

> 🚨코드가 오히려 복잡해진 것 같은데 스프링 컨테이너를 사용하는 이유는 무엇일까?
>
> 스프링 컨테이너로 의존성을 관리하게 되면 조금 더 제공되는 기능이 많다.가령 컨테이너에 있는 객체는 싱글톤으로 관리되어 메모리 사용양을 줄이거나 하는 등의 기능을 제공한다. 그리고 중요한점은 제어 흐름을 개발자가 관리하는것이 아닌 프레임워크가 제어하도록 맡긴다는 부분이다. 복잡한 의존관계 관리등을 프레임워크에 위임하고 개발자는 자신이 개발할 코드에만 집중할 수 있다.



## 스프링 컨테이너 생성

```java
ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
```

- `ApplicationContext`를 스프링 컨테이너라고 한다.

- `ApplicationContext`는 인터페이스이다.ㅣ

- **스프링 컨테이너는 XML 설정정보를 기반으로 만들 수도 있고, 애노테이션 기반의 자바 설정 클래스로도 만들 수 있다.**

- 자바 설정 클래스를 기반으로 만드는 스프링 컨테이너가 바로

   

  ```
  new AnnotationConfigApplicationContext(AppConfig.class)
  ```

  이다.

  - 이 클래스는 `ApplicationContext`의 구현체이다.

> 참고로 더 정확히는 스프링 컨테이너를 부를 때, `BeanFactory, ApplicationContext`로 구분해서 이야기한다. `BeanFactory`를 직접 사용하는 경우는 없으므로 일반적으로 `ApplicationContext`를 스프링 컨테이너라고 한다. (`ApplicationContext`는 `BeanFactory`를 상속한다 -> `BeanFactory`에 추가 기능을 더했다.)

#### 📌 스프링 컨테이너 생성과정

#### 1. 스프링 컨테이너 생성

![img](https://media.vlpt.us/images/syleemk/post/9a09457f-60db-4027-875c-709081d850d4/image.png)

- `new AnnotationConfigApplicationContext(AppConfig.class)`
- 스프링 컨테이너를 생성할 때는 구성정보(설정정보)를 지정해주어야한다.
- 여기서는 `AppConfig.class`를 구성정보로 지정했다.

#### 2. 스프링 빈 등록

![img](https://media.vlpt.us/images/syleemk/post/50009fb1-ef26-4b52-9ba5-fc69195db5ab/image.png)

- 스프링 컨테이너는 파라미터로 넘어온 설정 클래스 정보를 사용해서 스프링 빈을 등록한다.
- 빈 이름은 메서드 이름을 사용한다(key로 사용한다)
- 빈 이름을 직접 부여할 수도 있다.
  - `@Bean(name="memberService2")`

> 🚨주의 : 빈 이름은 항상 서로 다른 이름을 부여해야한다. 같은 이름을 부여하면, 다른 빈이 무시되거나, 기존 빈을 덮어버리거나, 설정에 따라 오류가 발생한다.

#### 3. 스프링 빈 의존관계 설정 - 준비

![img](https://media.vlpt.us/images/syleemk/post/b8de2750-d800-4d3c-9bfb-bd4bf32d84a7/image.png)

#### 4. 스프링 빈 의존관계 설정 - 완료

![img](https://media.vlpt.us/images/syleemk/post/8832021b-251c-465d-9ac4-77455679ef66/image.png)

- 스프링 컨테이너는 설정 정보를 참고해서 의존관계를 주입(DI)한다.
- 단순히 자바 코드를 호출하는 것 같지만, 차이가 있다.
- 스프링 컨테이너는 등록된 스프링 빈들을 싱글톤 패턴으로 생성하고 관리해준다.

> 스프링은 빈을 생성하고, 의존관계를 주입하는 단계가 나누어져있다. 먼저 스프링 빈 객체를 모두 생성하고 그다음에 연결해준다.
> 그런데 이렇게 자바 코드로 스프링 빈을 등록하면 **생성자를 호출하면서 의존관계 주입도 "한번에" 처리된다.**  왜냐면 memberService를 생성하면 new MemberServiceImpl이 생성되면서 memberRepository 를 호출함 그러면 여기서는 MemoryMemberRepository가 생성됨...... 하나를 부르는 순간 다 엮여져버림 
>
> 여기서는 이해를 위해 개념적으로 나누어 설명했다. 실제로는 의존관계가 한번에 설정이되고 이렇게 단계적으로 되지 않음 그런데 실제 스프링 라이프사이클과 의존관계는 나누어져 있음 
>
> 자세한 내용은 의존관계 주입에서 다시 설명한다.

