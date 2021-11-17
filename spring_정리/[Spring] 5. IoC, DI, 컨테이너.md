## 제어의 역전 IoC(Inversion of Control)

```java
public class MemberServiceImpl implements MemberService {

   // 구체적인 구현 객체에 의존한다 -> DIP 위반
   private final MemberRepository = new MemoryMemberRepository();
  ...
}
```

- **기존의 프로그램은 클라이언트 구현 객체가 스스로 필요한 서버 구현 객체를 생성하고, 연결하고, 실행**했다. 한마디로 **구현 객체가 프로그램의 제어 흐름을 스스로 조종**했다. 개발자 입장에서는 자연스러운 흐름이다.
  - 내가 개발을 하다가 필요한 객체가 있으면 new하고 또 그안에서 가다가 필요한게있으면 호출하고.. 이런 자연스러운 흐름
- 기존 프로그램은 개발자가 필요한 객체 생성하고 호출하고 실행시키고 했는데, IoC는 그러한 실행의 흐름을 프레임워크같은 것이 대신 해주는 것을 말한다.

```java
//구현체를 생성하고 연결해주는 역할을 분리 (책임을 분리)
//SRP(단일 책임 원칙) 준수
public class AppConfig {

    public MemberService memberService() {
        //구현객체를 생성하고, 생성자를 통해 의존성을 주입해준다
        return new MemberServiceImpl(memberRepository());
    }

    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

    public OrderService orderService() {
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    public DiscountPolicy discountPolicy() {
//        return new FixDiscountPolicy();
        return new RateDiscountPolicy();
    }
}

public class MemberServiceImpl implements MemberService {

    //더이상 구현에 대한 의존이 없다, 인터페이스에만 의존한다 -> DIP 준수
   private final MemberRepository memberRepository;

   //AppConfig에 의해 의존성을 주입받는다
   public MemberServiceImpl(MemberRepository memberRepository) {
      this.memberRepository = memberRepository
   }
}
```

- 반면에 AppConfig가 등장한 이후에 구현 객체는 자신의 로직을 실행하는 역할만 담당한다. (SRP 단일 책임 원칙 준수)
- AppConfig의 등장으로 애플리케이션은 크게 사용영역과, 객체를 생성하고 구성(Configuration)하는 영역으로 분리되었다.
- 프로그램의 제어의 흐름은 이제 AppConfig가 가져간다. 예를 들어서 MemberServiceImpl은 필요한 인터페이스를 호출하지만(MemberRepository), **어떤 구현객체들이 실행될지 모른다.**
- 프로그램에 대한 제어 흐름에 대한 권한은 모두 AppConfig가 가지고있다. 심지어 OrderServiceImpl도 AppConfig가 생성한다. 그리고 AppConfig는 OrderServiceImpl이 아닌 OrderService 인터페이스의 다른 구현 객체를 생성하고 실행할 수도 있다. 그런 사실도 모른채 OrderServiceImpl은 묵묵히 자신의 로직을 실행할 뿐이다.

> 이렇듯 프로그램의 **제어 흐름**을 직접 제어하는 것이 아니라 **외부에서 관리하는 것**을 **제어의 역전(IoC)**라고 한다.

## 프레임워크 vs 라이브러리

- 프레임워크는 내가 작성한 코드를 제어하고, 대신 실행해준다 (JUnit)
  - 테스트 코드
- 반면에 내가 작성한 코드가 직접 제어의 흐름을 담당한다면 그것은 프레임워크가 아니라 라이브러리다.
  - 자바 객체를 json으로 바꿀때 라이브러리는 불러다가 직접호출함 이런건 라이브러리



```java
class MemberServiceTest {

    MemberService memberService;
    @BeforeEach
    public void beforeEach() {
        AppConfig appConfig = new AppConfig();
        memberService = appConfig.memberService();
    }

    @Test
    void join() {
//given
        Member member = new Member(1L, "memberA", Grade.VIP);
//when
        memberService.join(member);
        Member findMember = memberService.findMember(1L);
//then
        Assertions.assertThat(member).isEqualTo(findMember);
    }
}
```

우리는 테스트코드를 작성할때 join안에 있는 로직만 개발을 진행한다. 이 테스트 코드에 대한 실행, 제어권은 junit이라는 테스트 프레임워크가 가져가고 대신 실행하게 된다.

junit은 코드를 실행할때 그냥 실행하는 것이 아니라 자신만의 **라이프 사이클**을 따라 테스트를 실행한다. 위 코드로 대략적인 예를 들면 @BeforeEach 먼저실행하고 @Test 실행하는 방식으로 진행한다. 

한마디로 프레임워크의 정해진 라이프 사이클 속에서 콜백식으로 자신이 작성한 코드가 불러지는 형태이다. 이렇듯 **내가 제어권을 가진게 아니라 필요한 부분만 딱 개발하면 그게 프레임워크에 적절한 타이밍에 호출**이 되는 것이다. 그리고 이렇게 호출하는 제어권을 넘기는 것을 **제어의 역전**이라 말한다.





```java
public class MemberApp {
    public static void main(String[] args) {

        AppConfig appConfig = new AppConfig();
        MemberService memberService = appConfig.memberService();

//        MemberService memberService = new MemberServiceImpl();
        Member member = new Member(1L, "memberA", Grade.VIP);
        memberService.join(member);
        Member findMember = memberService.findMember(1L);
        System.out.println("new member = " + member.getName());
        System.out.println("find Member = " + findMember.getName());
    }
}
```

MemberApp에서 main 메서드를 실행하는 것은 보통 개발자들이 직접 제어함

필요한거 new해서 돌리고 이런거는 개발자가 직접 순차적으로 돌리는 것





## 의존관계 주입 DI(Dependency Injection)

- OrderServiceImpl은 DiscountPolicy 인터페이스에 의존한다. **실제 어떤 구현 객체가 사용될지는 모른다.** (FixDiscountPolicy를 사용할지 RateDiscountPolicy사용할지 모름)
- 의존관계는 **정적인 클래스 의존관계**와 **실행시점에 결정되는 동적인 객체(인스턴스) 의존관계** 둘을 분리해서 생각해야한다.

#### 정적인 클래스 의존관계

![img](https://media.vlpt.us/images/syleemk/post/916aede7-7ae1-4685-a925-5207108eb777/image.png)

- 클래스가 사용하는 import 코드만 보고 의존관계를 쉽게 판단할 수 있다.
- 정적인 의존관계는 **애플리케이션을 실행하지 않아도 분석**할 수 있다. (show dependency하면 보임)
- 클래스 다이어그램을 보면 OrderServiceImpl은 MemberRepository와 DiscountPolicy 인터페이스에 의존한다는 것을 알 수 있다.
- 그런데 이러한 **클래스 의존관계만으로는 실제 어떤 객체가 OrderServiceImpl에 주입되는지 알 수 없다.**

#### 동적인 객체(인스턴스) 의존관계

![img](https://media.vlpt.us/images/syleemk/post/2a9d9e4c-3ddf-4cfe-8df6-b26a3354ae3a/image.png)

- 애플리케이션 실행 시점에 실제 생성된 객체 인스턴스의 참조가 연결된 의존관계이다.
- 애플리케이션을 실행시켜봐야 어떤 객체가 주입되었는지 알 수 있다. (FixDiscountPolicy를 사용할지 RateDiscountPolicy사용할지 모름)
- 애플리케이션 **[실행 시점(런타임)](#### 런타임)**에 외부(AppConfig같은 DI 컨테이너)에서 실제 구현 객체를 생성하고 클라이언트에 전달해서 클라이언트와 서버의 실제 의존관계가 연결되는 것을 **의존관계 주입**이라고 한다.
- 객체 인스턴스를 생성하고 그 참조값을 전달해서 연결된다.
- 의존관계 주입을 사용하면 클라이언트 코드를 변경하지 않고, 클라이언트가 호출하는 대상의 인스턴스 타입을 변경할 수 있다.
- **의존관계 주입을 사용하면 정적인 클래스 의존관계를 변경하지 않고, 동적인 객체 인스턴스 의존관계를 쉽게 변경할 수 있다.**

## IoC 컨테이너, DI 컨테이너

- AppConfig처럼 객체를 생성하고 관리하면서 의존관계 연결해주는 것을
- IoC 컨테이너 혹은 DI 컨테이너라고 한다.
- 의존관계 주입에 초점을 맞추어서 요즘에는 DI 컨테이너라고 한다. (어샘블러,오브젝트 팩토리등 으로도 불림)







#### 런타임

프로그램을 생성하기 위해 개발자는 첫째로 소스코드를 작성하고 컴파일이라는 과정을 통해 기계어코드로 변환 되어 실행 가능한 프로그램이 되며, 이러한 편집 과정을 컴파일타임(Compiletime) 이라고 부른다.

컴파일과정을 마친 프로그램은 사용자에 의해 실행되어 지며, 이러한 응용프로그램이 동작되어지는 때를 런타임(Runtime)이라고 부른다.



의존관계의 "주입"은 스프링이 로드될 때 발생합니다. 컴파일 시점이 의존관계가 어떻게 맺어질지에 대한 것이라면 런타임 시점은 실제 의존관계의 주입이 일어나는 것입니다. 컴파일 시점에는 스프링 컨테이너가 로드되지 않습니다. 런타임시 스프링 컨테이너가 로드되고 그때 빈으로 등록하는 과정에서 의존관계 주입발생