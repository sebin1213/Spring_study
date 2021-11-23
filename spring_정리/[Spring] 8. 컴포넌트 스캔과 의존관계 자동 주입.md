## 컴포넌트 스캔과 의존관계 자동 주입

- 지금까지는 스프링 빈을 등록할 때, 자바 코드의 @Bean이나 XML의 <Bean\> 등을 통해서 설정 정보에 직접 등록할 스프링 빈을 나열했다.
- 예제에서는 몇 개 안되었지만, 이렇게 등록해야할 스프링 빈이 수십, 수백개가 된다면 일일이 등록하기도 힘들고, 설정 정보도 커지고, 누락하는 문제도 발생한다.
- 그래서 스프링 빈은 설정 정보가 없어도 자동으로 스프링 빈을 등록하는 **컴포넌트 스캔**이라는 기능을 제공한다.
- 또 의존관계도 자동으로 주입하는 `@Autowired`라는 기능도 제공한다.

> 지금까지는 @Configuration이 붙은 자바 설정 파일에 @Bean 어노테이션으로 일일이 스프링 빈을 등록하고, 또 생성자를 통해서 일일이 의존성을 주입해주었다.

```java
@Configuration
@ComponentScan
public class AutoAppConfig {

}
```

- 컴포넌트 스캔을 사용하려면 먼저 설정정보에 `@ComponentScan`을 붙여주면 된다!
- 기존의 AppConfig와 다르게 @Bean으로 등록한 클래스가 하나도 없다!

> 컴포넌트 스캔은 이름 그대로 `@Component`애노테이션이 붙은 클래스를 스캔해서 스프링 빈으로 등록한다. `@Component`를 붙여주자!

> 참고로 `@Configuration`도 컴포넌트 스캔의 대상이다. `@Configuration`코드 열어보면 `@Component`애노테이션이 붙어있다.

#### MemoryMemberRepository @Component 추가

```java
@Component
public class MemoryMemberRepository implements MemberRepository {}
```

#### RateDiscountPolicy @Component 추가

```java
@Component
public class RateDiscountPolicy implements DiscountPolicy {}
```

#### MemberServiceImpl @Component, @Autowired 추가

```java
@Component
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    
    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
  }
}
```

- 이전 AppConfig의 경우 `@Bean`으로 직접 설정 정보를 작성하고, **의존관계도 직접 명시**했다.
- 하지만 이제는 더이상 이런 설정 정보 자체가 없기 때문에, 의존관계 주입도 각각의 `@Component`클래스 안에서 해결해야한다.
- `@Autowired`는 의존관계를 자동으로 주입해준다.

### 컴포넌트 스캔과 자동 의존관계 주입 동작 방식

#### 1. @ComponentScan

![img](https://media.vlpt.us/images/syleemk/post/0b5c1948-cef8-4e19-aad4-ef72e292c904/image.png)

- `@ComponentScan`은 `@Component`가 붙은 모든 클래스를 스프링 빈으로 등록한다.
- 이때 스프링 빈의 기본 이름은 클래스 명을 사용하되, 맨 앞글자만 소문자를 사용한다.
  - 빈 이름 기본전략 : MemberServiceImpl클래스 ➡ memberServiceImpl
  - 빈 이름 직접 지정 : 만약 스프링 빈의 이름을 직접 지정하고 싶으면 `@Component("memberservice2")` 이런식으로 이름을 부여하면 된다.

#### 2. @Autowired 의존관계 자동 주입

![img](https://media.vlpt.us/images/syleemk/post/fcef0dfb-697c-4eb7-950d-cf317f60596f/image.png)

- 생성자에 `@Autowired`를 지정하면, 스프링 컨테이너가 자동으로 해당 스프링 빈을 찾아서 주입한다.

- 이때 기본 조회 전략은 (빈 조회 전략)

   

  타입이 같은 빈

  을 찾아서 주입한다.

  - `getBean(MemberRepository.class)`와 동일하다고 이해하면 된다.
  - 이전에 말했듯이 부모타입으로 빈 조회하면, 자식타입 빈 객체까지 모두 조회한다.

![img](https://media.vlpt.us/images/syleemk/post/73fcab3e-9565-44e1-bb9c-bee05451c1cd/image.png)

- 생성자에 파라미터가 여러 개여도 다 찾아서 자동으로 주입한다.



### 탐색 위치와 기본 스캔 대상

#### 탐색할 패키지의 시작 위치 지정

- 모든 자바 클래스를 다 컴포넌트 스캔하면 시간이 오래 걸린다. (외부 라이브러리까지 다 탐색할 수는 없다.)
- 따라서 꼭 필요한 위치부터 탐색하도록 시작 위치를 지정할 수 있다.

```java
@ComponentScan(
    basePackages = "hello.core"
)
```

- ```
  basePackages
  ```

   

  : 탐색할 패키지의 시작위치를 지정한다. 이 패키지를 포함해서 하위 패키지를 모두 탐색한다.

  - `basePackages = {"hello.core", "hello.service"}`처럼 여러 시작위치를 지정할 수도 있다.

- `basePackagesClasses` : 지정한 클래스의 패키지를 탐색 시작 위치로 지정한다.

- ✨**만약 지정하지 않으면 `@ComponentScan`이 붙은 설정 정보 클래스 패키지가 탐색 시작 위치가 된다.**

> ✨권장하는 방법은, 패키지 위치를 지정하지 않고, 설정 정보 클래스를 프로젝트 최상단에 두는 것이다. 최근 스프링 부트도 이 방법을 기본으로 제공한다.

- 프로젝트 최상단에 설정정보 위치시키면 **해당 패키지를 포함한 하위 패키지는 모두 자동으로 컴포넌트 스캔의 대상**이 된다.

> 참고로 스프링 부트를 사용하면 스프링 부트의 대표 시작 정보인 `@SpringBootApplication`을 이 프로젝트의 시작 루트 위치에 두는 것이 관례이다. (그리고 이 설정안에 `@ComponentScan`이 포함되어있다.)





컴포넌트 스캔 기본 대상
컴포넌트 스캔은 @Component 뿐만 아니라 다음과 내용도 추가로 대상에 포함한다.

* @Component : 컴포넌트 스캔에서 사용

* @Controlller : 스프링 MVC 컨트롤러에서 사용

* @Service : 스프링 비즈니스 로직에서 사용

* @Repository : 스프링 데이터 접근 계층에서 사용

* @Configuration : 스프링 설정 정보에서 사용

  

  해당 클래스의 소스 코드를 보면 @Component 를 포함하고 있는 것을 알 수 있다.


```java
  @Component
  public @interface Controller {
  }
  @Component
  public @interface Service {
  }
  @Component
  public @interface Configuration {
  }
```

> 참고: 사실 애노테이션에는 상속관계라는 것이 없다. 그래서 이렇게 애노테이션이 특정 애노테이션을 들고
> 있는 것을 인식할 수 있는 것은 자바 언어가 지원하는 기능은 아니고, 스프링이 지원하는 기능이다.

  컴포넌트 스캔의 용도 뿐만 아니라 다음 애노테이션이 있으면 스프링은 부가 기능을 수행한다.

- @Controller : 스프링 MVC 컨트롤러로 인식 
- @Repository : 스프링 데이터 접근 계층으로 인식하고, 데이터 계층의 예외를 스프링 예외로 변환해준다.
- @Configuration : 앞서 보았듯이 스프링 설정 정보로 인식하고, 스프링 빈이 싱글톤을 유지하도록 추가 처리를 한다.
- @Service : 사실 @Service 는 특별한 처리를 하지 않는다. 대신 개발자들이 핵심 비즈니스 로직이 여기에 있겠구나 라고 비즈니스 계층을 인식하는데 도움이 된다.

>참고: useDefaultFilters 옵션은 기본으로 켜져있는데, 이 옵션을 끄면 기본 스캔 대상들이 제외된다.
  그냥 이런 옵션이 있구나 정도 알고 넘어가자.



## 의존관계 자동 주입

### 다양한 의존관계 주입 방법

의존관계 주입은 크게 4가지 방법이 있다.

1. 생성자 주입
2. 수정자 주입(setter 주입)
3. 필드 주입
4. 일반 메서드 주입

#### 🟥 생성자 주입

- 이름 그대로 생성자를 통해서 의존관계를 주입받는 방법
- 지금까지 우리가 진행했던 방법이 바로 생성자 주입이다
- 특징
  - 생성자 호출 시점에 딱 1번만 호출되는 것이 보장
    : 맨처음에 초기화 된 후, 이후 변경을 막을 수 있다.
  - **불변, 필수** 의존관계에 사용!! (항상 그런 것은 아니고 주로 그렇게 사용한다)

**불변**
개발에서 **불변**은 굉장히 중요! 내가 처음에 값 세팅한 후 더이상의 변경 허용하지 않을 수 있음! (**수정자 setter 메서드 안만들어 놓으면 됨**, 버그 확률 확 줄어든다.)

**필수**
필드를 `private final`로 설정해두면 무조건 값이 초기화되어야한다. 언어적으로 이 필드는 무조건 값을 세팅해달라고 잡은 것임.
따라서 해당 필드가 초기화되어있지 않으면 컴파일 오류를 발생시킨다.
생성자로 해당 필드를 **무조건** 초기화해야한다. (= 필수)
외부에서 호출할때도 생성자에 매개변수에 null 넣어서 호출안한다.
생성자에 매개변수 명시되어있으면, null허용한다는 말 없는이상 다 채워넣어서 호출한다.

```java
@Component
public class OrderServiceImpl implements OrderService {
    
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;
    
    //@Autowired 보고 스프링 컨테이너에서 매개변수에 해당하는 스프링 빈 꺼내서 주입해준다.
    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }
}
```

> **생성자가 딱 1개만 있으면 @Autowired를 생략**해도 자동 주입된다! (물론, 스프링 빈에만 해당된다)

#### 🟧 수정자 주입 (setter 주입)

- setter라 불리는 필드의 값을 변경하는 수정자 메서드를 통해서 의존관계를 주입하는 방법
- 특징
  - **선택, 변경** 가능성이 있는 의존관계에서 사용
    : 생성자 주입과 반대되는 특성
  - 자바빈 프로퍼티 규약의 수정자 메서드 방식을 사용하는 방법

**➕ 자바빈 프로퍼티 규약**

> 자바빈 프로퍼티, 자바에서는 과거부터 필드의 값을 변경하거나 가져올 때 직접 접근하지 않고, setXXX getXXX라는 메서드를 통해서 값을 읽거나 수정하는 규칙을 만들었는데, 그것이 자바빈 프로퍼티 규약!

```java
@Component
public class OrderServiceImpl implements OrderService {
	private MemberRepository memberRepository;
	private DiscountPolicy discountPolicy;

	@Autowired
	public void setMemberRepository(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}
    
	@Autowired
	public void setDiscountPolicy(DiscountPolicy discountPolicy) {
		this.discountPolicy = discountPolicy;
	}
}
```

- Setter에 @Autowired 애노테이션 없으면 연관관계 주입되지 않는다.
- 수정자 주입은 위 코드에서 MemberRepository가 스프링 빈에 등록이 되지 않았을 때도 사용가능하다. ➡ **선택적 의존관계 주입**

> 참고로 `@Autowired`의 기본 동작은 주입할 대상이 없으면 오류가 발생. 주입할 대상이 없어도 동작하게 하려면 `@Autowired(required = false)`로 지정하면 된다. ( 선택적으로 의존관계를 주입할때도 사용, 해당 메서드가 있어도 되고 없어도 될때 사용)

- 스프링 컨테이너는 크게 2가지 라이프사이클이 있다.
  - 스프링 빈 등록
  - 의존관계 자동 주입 : 빈 등록을 마친 후, @Autowired 걸린 애들을 주입해준다.

> 스프링은 **빈 등록하는 단계**와 **의존관계 주입해주는 단계** 나눠져있다.

이전 게시물에 빈 생성과 등록, 의존관계 주입하는 단계에 대해 설명했었다. 스프링 빈 등록한 후, 의존관계를 주입하는 단계를 따로 구분했었는데, 그 단계가 바로 @Autowired에 의존관계 주입해주는 단계였다.

**그런데!**

생성자 주입은 좀 특이하다.🤔

스프링이 빈 등록된 객체를 생성하기 위해서는 어쩔 수 없이 생성자를 호출해야한다.

**따라서 생성자 호출되면서 동시에 의존관계도 같이 주입된다!!**

> 생성자 주입은 빈 등록과 의존관계 주입이 **같이** 일어난다.

참고로 생성자 주입과 setter 주입 같이 있어도, 둘 다 일어난다.

먼저 생성자 주입이 일어난 후, setter 주입이 일어난다.

#### 🟨 필드 주입

- 이름 그대로 필드에 바로 주입하는 방법

```java
@Component
public class OrderServiceImpl implements OrderService {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private DiscountPolicy discountPolicy;
}
```

- 특징
  - 코드가 간결해서 많은 개발자들을 유혹하지만! **외부에서 변경이 불가능**해서 **테스트하기 힘들다**는 치명적인 단점이 있다!👿
  - 예를 들어 db접근을 안하고 더미데이터를 넘기는?? 가짜 memberRepository를 만들어서 테스트하고싶다고 할때 쟤를 바꿀 방법이 없음.. 데이터를 넣어서 테스트할라면 set을 또 만들어서 테스트해야함
  - **DI 프레임워크가 없으면 아무것도 할 수 없다.** 😨
  - 따라서 스프링 컨테이너 띄우지 않고 순수한 Java로 하는 단위테스트가 불가능하다!
  - 사용하지말자❗❗❗ 하지만 아래에서는 사용해도 됨
    - 애플리케이션의 실제코드와 관련 없는 테스트코드
    - 스프링 설정을 목적으로하는 `@Configuration`같은 곳**에서**만 특별한 용도로 사용

#### 🟩 일반 메서드 주입

- 생성자가 아니라 일반 메서드를 통해서 주입받을 수 있다.

```java
@Component
public class OrderServiceImpl implements OrderService {
    private MemberRepository memberRepository;
    private DiscountPolicy discountPolicy;
    @Autowired
    public void init(MemberRepository memberRepository, DiscountPolicy
    discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }
}
```

- 특징
  - 한번에 여러 필드를 주입받을 수 있다.
  - 일반적으로 잘 사용하지 않는다. (생성자 주입이나 수정자 주입안에서 다 해결됨)

> 참고로 당연한 이야기지만 의존관계 자동 주입은 스프링 컨테이너가 관리하는 스프링 빈이어야 동작한다. 스프링 빈이 아닌 `Member`같은 클래스에서 `@Autowired`코드를 적용해도 아무기능도 동작하지 않는다.

### 생성자 주입을 선택해라!

> 과거에는 수정자 주입과 필드 주입을 많이 사용했지만, 최근에는 스프링을 포함한 DI 프레임워크 대부분이 생성자 주입을 권장한다. 그 이유는 다음과 같다.

#### 🔴 불변

- 대부분의 의존관계 주입은 한번 일어나면 애플리케이션 종료 시점까지 의존관계를 변경할 일이 없다. 오히려 대부분의 의존관계는 애플리케이션 종료시까지 변하면 안된다!(**불변**해야한다)
- 수정자 주입을 사용하면, setXXX 메서드를 public으로 열어둬야한다.
- 누군가 실수로 변경할 수도 있고, 변경하면 안되는 메서드를 열어두는 것은 좋은 설계방법이 아니다.
- 생성자 주입은 객체를 생성할 때 딱 1번만 호출되므로 이후에 호출되는 일이 없다. 따라서 **불변**하게 설계할 수 있다.

#### 🟠 누락을 알려줌

프레임워크 없이 순수한 자바 코드를 단위테스트하는 경우 (**굉장히 많음!!**)
다음과 같이 수정자 의존관계인 경우

```java
public class OrderServiceImpl implements OrderService {
    
    private MemberRepository memberRepository;
    private DiscountPolicy discountPolicy;
    
    @Autowired
    public void setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    
    @Autowired
    public void setDiscountPolicy(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }
}
```

- **`@Autowired`가 프레임워크 안에서 동작할 때는 의존관계가 없으면 오류가 발생**하지만, 지금은 **프레임워크 없이** 순수한 자바 코드로만 단위테스트를 수행하고 있다. ➡ 따라서 스프링이 없으므로 스프링 오류 발생시키지 않는다.

```java
@Test
void createOrder() {
    //의존관계 주입 해주지 않았다. (setter 호출안함)
    OrderServiceImpl orderService = new OrderService();
    orderService.createOrder(1L, "itemA", 10000);
}
```

- 위처럼 테스트하면 실행은된다
- 하지만 실행결과는 NPE(Null Point Exception)이 발생한다.
- memberRepository, discountPolicy 모두 의존관계 주입이 누락되었기 때문
  - 가짜 memberRepository라도 만들어서 주입해야한다.
  - 하지만 테스트를 작성하는 입장에서는 의존관계가 뭐가 들어가있는지 일일히 코드를 까봐야 알 수 있음


**BUT 생성자 주입**을 사용한다면 다음처럼 주입 데이터를 누락했을 때 **컴파일 오류**가 발생한다.
그리고 IDE에서 바로 어떤 값을 필수로 주입해야 하는지 알 수 있다. 실행하지 않더라도 코드에서 오류가 발생했음을 미리 알려준다.(컴파일 오류)

```java
@Test
void createOrder() {
    OrderServiceImpl orderService = new OrderServiceImpl(); //()에 빨간 밑줄 오류발생 그리고 주입해야하는 값 알려줌
    orderService.createOrder(1L, "itemA", 10000);
}
```

오류가 발생하지 않으려면

```java
@Test
void createOrder() {
    MemoryMemberRepository memberRepository = new MemoryMemberRepository();
    memberRepository.save(new Member(1L, "name", Grade.VIP));
    
    OrderServiceImpl orderService = new OrderServiceImpl(memberRepository, new FixDiscountPolicy());
    Order order = orderService.createOrder(1L, "itemA", 10000);
    assertThat(order.getDiscountPrice()).isEqualTo(1000);
}
```

이렇게 순수 자바코드로 하나하나 조립하듯이 필요한 정보를 넣어줘야함



#### 🟡 final 키워드 사용가능

생성자 주입을 사용하면 필드에 `final`키워드를 사용할 수 있다. 그래서 생성자에 혹시라도 값이 설정되지 않는 오류를 **컴파일 시점**에 막아준다.

```java
@Component
public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;
    
    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
    }
}
```

- 잘 보면 필수 필드인 `discountPolicy`에 값을 설정해야하는데 이부분이 누락되었다.
- 자바는 컴파일 시점에 다음 오류를 발생시킨다.
- `java: variable discountPolicy might not have been initialized`
- 기억하자❗ **컴파일 오류는 세상에서 가장 빠르고, 좋은 오류다!**

> 참고로, 수정자 주입을 포함한 나머지 주입방식은 모두 생성자 이후에 호출되므로, 필드에 final 키워드를 사용할 수 없다!! 오직 생성자 주입에서만 final 키워드를 사용할 수 있다.

#### 정리

- 생성자 주입방식을 선택하는 이유는 여러가지가 있지만, 프레임워크에 의존하지 않고 순수한 자바 언어의 특징을 잘 살리는 방법이기도 하다.
- 기본적으로 생성자 주입을 사용하고, 필수 값이 아닌 경우에는 수정자 주입 방식을 옵션으로 부여하면된다. 생성자 주입과 수정자 주입을 동시에 사용할 수 있다.
- 항상 생성자 주입을 선택해라! 그리고 가끔 옵션이 필요하면 수정자 주입을 선택해라. 필드 주입은 사용하지 않는게 좋다!



## 롬복

막상 개발을 해보면, 대부분이 다 불변이고, 그래서 다음과 같이 생성자에 final 키워드를 사용하게 된다. 그런데 생성자도 만들어야 하고, 주입 받은 값을 대입하는 코드도 만들어야 하고…코드가 너무 길다..!!
필드 주입처럼 좀 편리하게 사용하는 방법은 없을까? 하고 나온게 롬복이라는 라이브러리다.



```java
@Component
public class OrderServiceImpl implements OrderService {
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;
    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy
    discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }
}
```

- 이제 롬복을 적용해보자. 롬복 라이브러리 적용 방법은 아래에 적어두었다.

- 롬복 라이브러리가 제공하는 @RequiredArgsConstructor 기능을 사용하면 final이 붙은 필드를 모아서 생성자를 자동으로 만들어준다. (다음 코드에는 보이지 않지만 실제 호출 가능하다.)

  

```java
@Component
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final MemberRepository memberRepository;
	private final DiscountPolicy discountPolicy;
}
```

매우 간단해졌다.!

- @RequiredArgsConstructor 필수값인 final 이 붙은거를 파라미터로 받는 생성자를 만들어줌
- 최근에 많이 사용함!



롬복 라이브러리 추가방법

<details>
<summary>bulid gradle 에 추가</summary>
	<div markdown="1">
        plugins {
        id 'org.springframework.boot' version '2.3.2.RELEASE'
        id 'io.spring.dependency-management' version '1.0.9.RELEASE'
        id 'java'
        }
        group = 'hello'
        version = '0.0.1-SNAPSHOT'
        sourceCompatibility = '11'
        //lombok 설정 추가 시작
        configurations {
            compileOnly {
                extendsFrom annotationProcessor
            }
        }
        //lombok 설정 추가 끝
        repositories {
            mavenCentral()
        }
        dependencies {
            implementation 'org.springframework.boot:spring-boot-starter'
        //lombok 라이브러리 추가 시작
            compileOnly 'org.projectlombok:lombok'
            annotationProcessor 'org.projectlombok:lombok'
            testCompileOnly 'org.projectlombok:lombok'
            testAnnotationProcessor 'org.projectlombok:lombok'
        //lombok 라이브러리 추가 끝
            testImplementation('org.springframework.boot:spring-boot-starter-test') {
                exclude group: 'org.junit.vintage', module: 'junit-vintage-engine'
            }
        }
        test {
            useJUnitPlatform()
        }
    </div>
</details>

   

또는 start.spring.io에서 dependence에 추가

![image-20211123194349548](C:\Users\hope\AppData\Roaming\Typora\typora-user-images\image-20211123194349548.png)

이것까지 해줘야 사용가능







### 조회한 빈이 2개이상 - 문제

`@Autowired`는 타입(Type)으로 조회한다.

```java
@Autowired
private DiscountPolicy discountPolicy;
```

타입으로 조회하기 때문에, 마치 다음 코드와 유사하게 동작 (실제는 더 많은 기능 제공)

- `ac.getBean(DiscountPolicy.class)`

타입으로 조회하면 선택된 빈이 2개 이상일 때 문제가 발생한다.
위의 코드에서 `DiscountPolicy`의 하위 타입인 `FixDiscountPolicy`,`RateDiscountPolicy` 둘다 스프링 빈(`@Component`)으로 선언했을 때 오류가 발생한다.

- `NoUniqueBeanDefinitionException` 오류 발생

```java
NoUniqueBeanDefinitionException: No qualifying bean of type
'hello.core.discount.DiscountPolicy' available: expected single matching bean
but found 2: fixDiscountPolicy,rateDiscountPolicy
```

오류 메시지가 하나의 빈을 기대햇는데, 2가지 빈을 발견했다고 알려준다.

이때 하위 타입으로 지정할 수도 있지만, 하위 타입으로 지정하는 것은 DIP를 위배하고 유연성(변경이 자유롭고 확장에 열려있는)이 떨어진다.
그리고 이름만 다르고 완전히 똑같은 타입의 스프링 빈이 2개 있을 때 해결이 안된다.
스프링 빈을 수동 등록해서 문제를 해결해도 되지만 (자동 수동에서는 수동이 우선시 된다, 수동 등록한 빈이 덮어쓰는 개념)
의존관계 자동 주입에서 해결하는 여러 방법이 있다.

### @Autowired 필드명, @Qualifier, @Primary

조회한 대상 빈이 2개 이상일 때 해결방법

- @Autowired 필드명 매칭
- @Qualifier ➡ @Qualifier 끼리 매칭 ➡ 빈 이름 매칭
- @Primary 사용

#### 🤔 @Autowired 필드 명 매칭

`@Autowired`는 타입 매칭을 시도하고, 이때 여러 빈이 있으면 필드 이름(생성자 주입의 경우 파라미터 이름)으로 빈 이름 추가 매칭한다.

**기존 코드**

```java
@Autowired
private DiscountPolicy discountPolicy;
```

**필드명을 빈 이름으로 변경**

```java
@Autowired
private DiscountPolicy rateDiscountPolicy;
```

필드 명이 `rateDiscountPolicy`이므로 정상 주입된다
**필드명 매칭은 먼저 타입 매칭을 시도하고 그 결과에 여러 빈이 있을 때 추가로 동작하는 기능이다.**

**@Autowired 매칭 정리**
\1. 타입 매칭
\2. 타입 매칭의 결과가 2개 이상일 때 의존성 주입받는 필드 명으로 빈 이름 매칭

#### 😙 @Qualifier 사용

`@Qualifier`는 **추가 구분자**를 붙여주는 방법이다. 주입시 추가적인 방법을 제공하는 것이지 빈 이름을 변경하는 것은 아니다.

**빈 등록시 @Qualifier를 붙여준다**

```java
//컴포넌트 스캔시
@Component
@Qualifier("mainDiscountPolicy")
public class RateDiscountPolicy implements DiscountPolicy {}

@Component
@Qualifier("fixDiscountPolicy")
public class FixDiscountPolicy implements DiscountPolicy {}

//직접 빈 등록시
@Bean
@Qualifier("mainDiscountPolicy")
public DiscountPolicy discountPolicy() {
	return new ...
}
```

- 주입 시에 @Qualifier를 붙여주고 등록한 이름을 적어준다.

**생성자 자동주입 예시**

```java
@Autowired
public OrderServiceImpl(MemberRepository memberRepository,
	@Qualifier("mainDiscountPolicy") DiscountPolicy discountPolicy) {
    this.memberRepository = memberRepository;
    this.discountPolicy = discountPolicy;
}
```

**수정자 자동주입 예시**

```java
@Autowired
public DiscountPolicy setDiscountPolicy(@Qualifier("mainDiscountPolicy")
					DiscountPolicy discountPolicy) {
	return discountPolicy;
}
```

`@Qualifier`로 주입할 때, `@Qualifier("mainDiscountPolicy")`를 못찾으면 어떻게 될까? 그러면 mainDiscountPolicy라는 이름의 스프링 빈을 추가로 찾는다. 하지만 경험상 `@Qualifier`는 `@Qualifier`를 찾는 용도로만 사용하는 것이 명확하고 좋다.

**@Qualifier 정리**
\1. @Qualifier끼리 매칭
\2. 빈 이름 매칭
\3. `NoSuchBeanDefinitionException` 예외 발생

#### 😘 @Primary 사용

`@Primary`는 우선순위를 정하는 방법이다
@Autowired 시에 여러 빈이 매칭되면 `@Primary`가 우선권을 가진다!

```java
@Component
@Primary
public class RateDiscountPolicy implements DiscountPolicy {}

@Component
public class FixDiscountPolicy implements DiscountPolicy {}
```

@Primary가 붙은 RateDiscountPolicy가 자동 의존관계 주입시 우선권 갖는다.

### @Primary vs @Qualifier

- `@Qualifier`의 단점은 주입 받을 때 모든 코드에 `@Qualifier`를 붙여주어야 한다는 점이다.
- 반면에 `@Primary`를 사용하면 이렇게 `@Qualifier`를 붙일 필요가 없다.

#### @Primary, @Qualifier 활용

코드에서 자주 사용하는 메인 데이터베이스의 커넥션을 획득하는 스프링 빈이 있고, 코드에서 특별한 기능으로 가끔 사용하는 서브 데이터베이스의 커넥션을 획득하는 스프링 빈이 있다고 했을 때, 메인 데이터베이스의 커넥션을 획득하는 스프링 빈은 `@Primary`를 적용해서 조회하는 곳에서 `@Qualifier`지정 없이 편리하게 조회하고, 서브 데이터의 커넥션 빈을 획득할 때는 `@Qualifier`를 지정해서 명시적으로 획득하는 방식으로 사용하면 코드를 깔끔하게 유지할 수 있다. 물론 이때 메인 데이터베이스의 스프링 빈을 등록할 때 `@Qualifier`를 지정해주는 것은 상관없다.

#### 우선순위

- `@Primary`는 기본값처럼 동작한다.
- `@Qualifier`는 매우 상세하게 동작한다.

스프링은 자동보다는 수동이, 넓은 범위의 선택권보다는 좁은 범위의 선택권이 우선 순위가 높다. 따라서 여기서도 `@Qualifier`가 우선권이 높다.