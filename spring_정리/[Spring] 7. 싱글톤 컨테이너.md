## 웹 애플리케이션과 싱글톤

- 스프링은 태생이 기업용 온라인 서비스 기술을 지원하기 위해 탄생
- 대부분의 스프링 애플리케이션은 웹 애플리케이션이다. 물론 웹이 아닌 애플리케이션도 얼마든지 개발할 수 있다.
- 웹 애플리케이션은 보통 여러 고객이 동시에 요청한다.

![img](https://media.vlpt.us/images/syleemk/post/5d6467ac-9ba3-49a2-9d5c-0a7df8385f0c/image.png)

- 스프링 없는 순수한 DI 컨테이너 AppConfig의 경우 **요청을 받을 때마다 객체를 새로 생성**한다.
- 고객 트래픽이 초당 100건이 나오면 초당 100개 객체가 생성되고 소멸된다! ➡ 메모리 낭비가 심하다.
- 해결방안은 해당 객체가 딱 1개만 생성되고, 공유하도록 설계하면 된다. ➡ 싱글톤 패턴

## 싱글톤 패턴

> 클래스의 인스턴스가 딱 1개만 생성되는 것을 보장하는 디자인 패턴

- 그래서 객체 인스턴스를 2개 이상 생성하지 못하도록 막아야한다!!
- private 생성자를 사용해서 **외부에서 임의로 new 키워드를 사용하지 못하도록** 막아야한다.

#### 코드 (main이 아닌 test영역에 위치)

```java
public class SingletonService {

    //1. static 영역에 객체를 딱 1개만 생성
    private static final SingletonService instance = new SingletonService();
    
    //2. public 으로 열어서 객체 인스턴스가 필요하면 이 static 메서드를 통해서만 조회하도록 허용한다.
    public static SingletonService getInstance() {
        return instance;
    }
    
    //3. 생성자를 private으로 선언해서 외부에서 new 키워드를 사용한 객체 생성을 하지 못하게 막는다.
    private SingletonService() {
    }
    
    public void logic() {
        System.out.println("싱글톤 객체 로직 호출");
    }
}
```

1. static 영역에 객체 instance를 미리 하나 생성해서 올려둔다.
2. 이 객체 인스턴스가 필요하면 오직 `getInstance()`메서드를 통해서만 조회할 수 있다.
3. 딱 1개의 객체 인스턴스만 존재해야하므로, 생성자를 private으로 막아서 혹시라도 외부에서 new 키워드로 객체 인스턴스가 생성되는 것을 막는다.

> 참고로 싱글톤 패턴을 구현하는 방법은 여러가지가 있다. 여기서는 객체를 미리 생성해두는 가장 단순하고 안전한 방법을 선택했다.

싱글톤 패턴을 적용하면 **고객의 요청이 올 때마다 객체를 생성하는 것이 아니라, 이미 만들어진 객체를 공유**해서 효율적으로 사용할 수 있다. 하지만 싱글톤 패턴은 다음과 같이 여러가지 문제점들을 가진다.

### 문제점

- 싱글톤 패턴을 구현하는 코드 자체가 많이 들어간다.
  - static 필드로 해당 인스턴스를 미리 생성해두고,
  - 만들어둔 인스턴스의 참조를 반환하는 메서드를 정의
  - private 생성자
  - 기본적으로 위의 3가지가 들어간다.
- 의존관계상 클라이언트가 구체 클래스에 의존한다. ➡ DIP 위반
  - getInstance() 한 것을 불러와야하기 때문에 인터페이스 참조로는 구현이 불가능
  - 구체 클래스를 참조해서 getInstance() 호출로 해당 인스턴스 참조 가져와야함
- 클라이언트가 구체 클래스에 의존하기 때문에 OCP 원칙을 위반할 가능성이 높다.
  - 구체 클래스가 바뀌면 클라이언트 코드도 바뀌어야하기 때문 ➡ OCP 위반
- 테스트하기 어렵다.
  - 이미 지정해서 가져옴 인스턴스를 미리 받아와서 설정이 끝나버림 유연하게 테스트하기 어려움
- 내부 속성을 변경하거나 초기화하기 어렵다.
- private 생성자로 자식 클래스를 만들기 어렵다. (상속한 클래스 생성하려면 부모 객체의 기본 생성자가 public으로 접근 가능해야한다.)
- 결론적으로 유연성이 많이 떨어진다.
  - DI 해주기도 참 어려움
  - 왜냐하면 구체클래스가져와서 getInstance() 해줘야하니까
- 안티패턴으로 불리기도 한다.

> 그런데 중간에 우리 했듯이 AppConfig같은 IoC 컨테이너를 두면 싱글톤도 DIP 위반 문제 해결되지 않나? 아래에 나와 같은 의문을 가진 분이 질문을 하였고 그에대한 답을 해주셨다.

![img](https://media.vlpt.us/images/syleemk/post/c23dc9a3-08b3-48bc-8952-c6724f0e5874/image.png)
![img](https://media.vlpt.us/images/syleemk/post/b4bb24e2-5a4a-4a4c-b337-7e4b08a594b4/image.png)
![img](https://media.vlpt.us/images/syleemk/post/63ed56b9-9bd7-41e5-8889-2d6c31961d65/image.png)

<참고자료>
[싱글톤의 문제점](https://jaygarl.wordpress.com/2017/02/01/싱글톤의-문제점/)
[싱글톤의 문제점2](https://elfinlas.github.io/2019/09/23/java-singleton/)

## 싱글톤 컨테이너

> 스프링 컨테이너는 싱글턴 패턴의 문제점을 해결하면서, 객체 인스턴스를 싱글톤(1개만 생성)으로 관리한다. 지금까지 우리가 학습한 스프링 빈이 바로 싱글톤으로 관리되는 빈이다.

- 스프링 컨테이너는 싱글톤 패턴을 적용하지 않아도, 객체 인스턴스를 싱글톤으로 관리한다.
- 스프링 컨테이너는 싱글톤 컨테이너 역할을 한다. 이렇게 싱글톤 객체를 생성하고 관리하는 기능을 **싱글톤 레지스트리**라고 한다.
- 스프링 컨테이너의 이러한 기능 덕분에 싱글톤 패턴의 모든 단점을 해결하면서 객체를 싱글톤으로 유지할 수 있다.
  - 싱글톤 패턴을 위한 지저분한 코드가 들어가지 않아도된다.
  - DIP, OCP, 테스트, private 생성자로부터 자유롭게 싱글톤을 사용할 수 있다.

#### 싱글톤 컨테이너 적용 후

![img](https://media.vlpt.us/images/syleemk/post/b0c31f8b-8332-4389-af62-f7e26ccf1ce4/image.png)

- 스프링 컨테이너 덕분에 고객의 요청이 올 때마다 객체를 생성하는 것이 아니라, 이미 만들어진 객체를 공유해서 효율적으로 재사용할 수 있다.

> 참고로 스프링의 기본 빈 등록방식은 싱글톤이지만, 싱글톤 방식만 지원하는 것은 아니다. 요청할 때마다 새로운 객체를 생성해서 반환하는 기능도 제공한다 (prototype scope, 웹 관련 scope 등)

## 싱글톤 방식의 주의점

> 싱글톤 객체는 전역에서 공유되는 객체이므로 멀티쓰레드 환경에서의 동시성 문제를 해결해야한다.

전역에서 공유된다는 것, 메모리 영역에서 프로세스 전체에서 공유된다는 것. (생성된 인스턴스는 JVM 메모리의 힙영역에 할당되니까!)
개별 쓰레드당 할당되는 것이 아니라는 것!

공유변수의 동시성 문제는 OS에서도 나오는 중요한 개념!
둘 이상의 스레드가 동시에 공유변수 (임계구역)에 접근할 때 발생하는 문제들이 많으므로 상호배제를 보장해주어야함!

아니면 아예 상태를 유지하지 않도록 설계해라! 공유하는 값이 없도록!
(static 변수나 멤버변수에 값을 유지하고 있지 않도록 설계 : stateless)

- 싱글톤 패턴이든, 스프링 같은 싱글톤 컨테이너를 사용하든, 객체 인스턴스를 **하나만 생성**해서 **공유**하는 싱글톤 방식은 여러 클라이언트가 하나의 같은 객체 인스턴스를 공유하기 때문에 싱글톤 객체는 **상태를 유지(Stateful)하게 설계하면 안된다.**

- 무상태(Stateless)로 설계

  해야한다.

  - 특정 클라이언트에 의존적인 필드가 있으면 안된다.
  - 특정 클라이언트가 값을 변경할 수 있는 필드가 있으면 안된다.
  - 🚨가급적 읽기만 가능해야한다.
  - 필드 대신에 자바에서 **공유되지 않는, ✨지역변수, 파라미터, ThreadLocal 등을 사용**해야한다.

- **스프링 빈(싱글톤 빈)의 필드에 공유 값을 사용하면 정말 큰 장애가 발생할 수 있다.**

#### 상태를 유지할 경우 발생하는 문제점 예시

```java
public class StatefulService {

    private int price; //상태를 유지하는 필드
    
    public void order(String name, int price) {
        System.out.println("name = " + name + "price = " + price);
        this.price = price; //특정 클라이언트에 의해 상태값이 변경된다.
    }
    
    public int getPrice() {
        return price;
    }
}
public class StatefulTest {

    @Test
    void statefulServiceSingleton() {
        ApplicationContext = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService statefulService1 = ac.getBean("statefulService", StatefulService.class);
        StatefulService2 = ac.getBean("statefulService", StatefulService.class);
        
        //ThreadA : A사용자 10000원 주문
        statefulService1.order("userA",10000);
        //ThreadB : B사용자 20000원 주문
        statefulService2.order("userB",20000);
        
        //ThreadA : 사용자 A주문 금액 조회
        int price = statefulService1.getPrice();
        //ThreadA : 사용자 A는 10000원 기대했지만, 기대와 다르게 20000원 출력
        
        Assertions.assertThat(statefulService1.getPrice()).isEqualTo(20000);
    
    }
}

static class TestConfig {
    
    @Bean
    public StatefulService statefulService {
        return new StatefulService();
    }
}
```

- 예제 단순화 위해 실제 쓰레드는 사용하지 않음

- ThreadA가 사용자A 코드를 호출하고 ThreadB는 사용자B 코드를 호출한다 가정하자

- statefulService의 price 필드는 공유되는 필드인데, 특정 클라이언트가 값을 변경한다.

- 사용자 A의 주문금액은 10000원이 되어야하는데, 20000원이라는 결과가 나왔다.

- 실무에서 이런 경우를 종종 보는데, 이로인해 정말 해결하기 어려운 문제들이 터진다.

- 진짜 공유필드는 조심해야한다! **스프링 빈은 항상 무상태(Stateless)로 설계**할 것!!

  

## @Configuration과 싱글톤

> `@Configuration`은 사실 싱글톤을 위해 존재한다

#### AppConfig코드를 보자

```java
@Configuration
public class AppConfig {

    @Bean
    public MemberService memberService() {
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
}
```

- MemberService 빈을 만드는 코드를 보면

   

  ```
  memberRepository()
  ```

  를 호출

  - 해당 메서드는 `new MemoryMemberRepository()` 호출

- orderService 빈을 만드는 코드도 동일하게

   

  ```
  memberRepository()
  ```

  를 호출

  - 이 메서드를 호출하면 `new MemoryMemberRepository()`를 호출

> 결과적으로 각각 다른 2개의 `MemoryMemberRepository`가 생성되면서 싱글톤이 깨지는 것처럼 보인다. 스프링 컨테이너는 어떻게 이 문제를 해결하는 것일까?

- 해당 빈들을 조회해서 확인해보면 `memberRepository`빈은 모두 같은 인스턴스를 참조하는 것을 알 수 있다.
- AppConfig의 자바 코드를 보면 memberRepository는 memberService 빈 등록될 때 한 번, orderService 빈 등록될 때 한 번, 마지막으로 memberRepository 자체로 등록될 때 한 번, 총 3번 호출되어야한다.
- 하지만 호출 결과 확인해보면 총 1번만 호출되는 것을 알 수 있다.



## @Configuration과 바이트 코드 조작의 마법

스프링 컨테이너는 싱글톤 레지스트리이다. 따라서 스프링 빈이 싱글톤이 되도록 보장해주어야한다. 그런데 스프링이 자바 코드까지 어떻게 할 수는 없다. 위의 자바 코드를 보면 분명 3번 호출이 되어야하는데, 한 번만 호출되고있다.
스프링은 이를 위해 **클래스의 바이트 코드를 조작하는 라이브러리**를 사용한다.
✨모든 비밀은 `@Configuration`을 적용한 `AppConfig`에 있다.

#### 코드

```java
@Test
void configurationDeep() {

    ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
    
    //AppConfig도 스프링 빈으로 등록된다.
    AppConfig bean = ac.getBean(AppConfig.class);
    
    System.out.println("bean = " + bean.getClass());
    // 출력: bean = class hello.core.AppConfig$$EnhancedBySpringCGLIB$$bd479d70
}
```

- `AnnotationConfigApplicationContext`의 파라미터로 넘긴 값도 스프링 빈으로 등록된다. 따라서 `AppConfig`도 스프링 빈이 된다.
- `AppConfig` 스프링 빈을 조회해서 클래스 정보를 출력해보자

```
bean = class hello.core.AppConfig$$EnhancedBySpringCGLIB$$bd479d70
```

만약 순수한 자바 클래스라면 아래와 같이 출력되어야한다.
`class hello.core.AppConfig`

그런데 예상과는 다르게 클래스 명에 xxxCGLIB가 붙으면서 복잡한 이름을 갖게된 것을 알 수 있다.

> 이것은 내가 만든 클래스가 아니라 스프링이 CGLIB라는 바이트 코드 조작 라이브러리를 사용해서 AppConfig 클래스를 상속받은 임의의 다른 클래스를 만들고, 그 다른 클래스를 스프링 빈으로 등록했기 때문이다.

![img](https://media.vlpt.us/images/syleemk/post/034c21a6-ad52-4149-94b0-7bc3d76c7f0c/image.png)

appconfig가 cglib라는 바이트코드 조작 랩을 가지고 상속받아서 다른 클래스인 appconfig@cglib을 만든다.

이름은 appConfig 이름을 가져가고, 실제 등록되는 스프링 빈은 @CGLIB 클래스의 인스턴스가 등록된다.

스프링이 CGLIB 바이트코드 조작 라이브러리를 사용하여 만든 클래스가 바로 스프링 빈이 싱글톤이 보장되도록 해준다. 아마도 다음과 같이 바이트 코드를 조작해서 작성되어있을 것이다. (실제로는 CGLIB의 내부 기술을 사용하는데 매우 복잡하다.)

#### AppConfig@CGLIB 예상 코드

```java
@Bean
public MemberRepository memberRepository() {
    if(memorymemberRepository가 이미 스프링 컨테이너에 등록되어있으면?) {
        return 스프링 컨테이너에서 찾아서 반환;
    } else { // 스프링 컨테이너에 없으면
        기존로직을 호출해서 MemoryMemberRepository를 생성하고 스프링 컨테이너에 등록
        return 반환;
    }
}
```

- @Bean이 등록된 메서드마다 이미 스프링 빈이 존재하면 존재하는 빈을 반환하고, 스프링 빈이 없으면 생성해서 스프링 빈으로 등록하고 반환하는 코드가 **동적으로 만들어진다.**
- 덕분에 싱글톤이 보장되는 것이다.
- AppConfig@CGLIB 이 memberRepository()를 오버라이드  함 memorymemberRepository가 이미 스프링 컨테이너에 등록되어있으면 스프링 컨테이너에서 찾아서 반환하고 스프링 컨테이너에 없으면 기존로직을 호출해서 MemoryMemberRepository를 생성하고 스프링 컨테이너에 등록한다.

> 참고로 AppConfig@CGLIB은 AppConfig의 자식 타입이므로, AppConfig 타입으로 조회 가능하다. (부모타입으로 빈 조회하면 자식 타입 빈들까지 다 조회하니까!)

### @Configuration을 적용하지 않고, @Bean만 적용한다면?

`@Configuration`을 붙이면 바이트 코드를 조작하는 CGLIB 기술을 사용해서 싱글톤을 보장하지만, 만약 `@Bean`만 적용하면 어떻게 될까?

`@Configuration`을 주석처리 한 후 테스트를 다시 실행해보자!

> 결론부터 말하면, 스프링 빈이 등록되긴 한다. 하지만, CGLIB으로 바이트코드 조작되어 싱글톤을 보장하는 빈이 등록되지 않고, 순수 자바 객체가 등록된다. (싱글톤 보장 X)

무슨 문제가 있을까?

스프링 빈 등록시 아까와 다르게 memberRepository가 3번 호출된다!!

memberService와 orderService에 주입되는 memberRepository 객체는 스프링에 등록된 빈 객체가 아닌, 메서드 호출로 새로 생성된 인스턴스가 주입된다.😫 (스프링 컨테이너가 관리하지 않는 객체다)

### 정리

- `@Bean`만 사용해도 스프링 빈으로 등록되지만, 싱글톤을 보장하지 않는다.
- `@Configuration`없이 사용한다면 `memberRepository()`처럼 의존관계 주입이 필요해서 메서드를 직접 호출할 때 싱글톤을 보장하지 않는다.
- 크게 고민할 것이 없다! 스프링 설정 정보는 항상 `@Configuration`을 사용하자!!