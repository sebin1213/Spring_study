## SOLID

> 클린코드로 유명한 로버트 마틴이 좋은 객체 지향 설계의 5가지 원칙을 정리

- SRP : 단일 책임 원칙
- OCP : 개방 폐쇄 원칙
- LSP : 리스코프 치환 원칙
- ISP : 인터페이스 분리 원칙
- DIP : 의존관계 역전 원칙

### SRP 단일 책임 원칙

**Single Responsibility Principle**

- 한 클래스는 하나의 책임만을 가져야 한다.
- 하나의 책임이라는 것은 모호하다.
  - 클 수도 있고, 작을 수도 있다.
  - 문맥과 상황에 따라 다르다.
- 중요한 기준은 변경이다. 변경이 있을 때 파급 효과가 적으면 단일 책임 원칙 잘 따른 것이다.
  - 예를 들어서 UI하나 변경하는데, SQL 코드부터 시작해서 애플리케이션 다 고쳐야 한다면 단일책임 원칙을 잘 지키지 못한 것

### OCP 개방 폐쇄 원칙

**Open Closed Principle**

- 소프트웨어 요소는 **확장에는 열려**있으나 **변경에는 닫혀**있어야 한다.
- 어떻게? 확장을 하려면 당연히 기존 코드 변경해야하지 않나?
- ✨**다형성**을 활용해보자!
- 인터페이스를 구현한 새로운 클래스를 하나 만들어서 새로운 기능을 구현
  - 새로운 클래스를 만드는 것은 기존 코드를 변경하는 것이 아님
- 지금까지 배운 역할과 구현의 분리를 생각해보자

#### 😫 문제점

변경전

```java
public class MemberService {
    private MemberRepository memberRepository = new MemoryMemberRepository();
}
```

변경후

```java
public class MemberService {
    //private MemberRepository memberRepository = new MemoryMemberRepository();
    private MemberRepository memberRepository = new JdbcMemberRepository();
}
```

오잉...? OCP안지켰는데??  적용하다보니 클라이언트 코드를 변경함

- MemberService 클라이언트가 구현 클래스를 직접 선택
  - `MemberRepository m = new MemoryMemberRepository(); //기존 코드`
  - `MemberRepository m = new JdbcMemberRepository(); //변경 코드`
- **구현 객체를 변경하려면 클라이언트 코드를 변경해야한다.**
- **분명 다형성을 사용했지만 OCP원칙을 지킬 수 없다.**
- 이 문제를 어떻게 해결해야 하나?

> 객체를 생성하고, 연관관계를 맺어주는 별도의 조립, 설정자가 필요하다!!

이 별도의 무언가가 바로 **"스프링"**

### LSP 리스코프 치환 원칙

**Liskov Substitution Principle**

- 프로그램의 객체는 프로그램의 정확성을 깨뜨리지 않으면서 하위 타입의 인스턴스로 바꿀 수 있어야한다.
- 다형성에서 하위 클래스는 인터페이스 규약을 다 지켜야 한다는 것, 다형성을 지원하기 위한 원칙, 인터페이스를 구현한 구현체는 믿고 사용하려면, 이 원칙이 필요하다.
- 단순히 컴파일에 성공하는 것을 넘어서는 이야기
- 예) 자동차 인터페이스의 엑셀은 앞으로 가라는 기능이다. 뒤로 가게 구현한다면 LSP를 위반한 것! 느리더라도 앞으로 가야함
  (단순히 구현여부만 따지는 것이 아님, 기능적으로 올바르게 구현했느냐를 보는 것임)

### ISP 인터페이스 분리 원칙

**Interface Segregation Principle**

- 특정 클라이언트를 위한 인터페이스 여러 개가 범용 인터페이스 하나보다 낫다.
- 자동차 인터페이스 ➡ 운전 인터페이스, 정비 인터페이스로 분리
- 사용자 클라이언트 ➡ 운전자 클라이언트, 정비사 클라이언트로 분리
- 분리하면 정비 인터페이스 자체가 변해도 운전자 클라이언트에 영향을 주지 않는다. (정비 인터페이스 변경해야하는 경우, 정비 인터페이스만 바꾸면 됨, 운전자 인터페이스까지 변경할 필요가 없어짐)
- 인터페이스가 명확해지고, 대체 가능성이 높아진다.
  - 아무래도 덩어리가 크면 그걸 다 구현하기 힘들기 때문에 대체 힘듦
  - 좀 더 명확한 역할의 인터페이스로 분리하면 구현 쉽고 대체 가능성 높아짐

### DIP 의존관계 역전 원칙

**Dependency Inversion Principle**

- 프로그래머는 **"추상화에 의존해야지 구체화에 의존하면 안된다"**
- 의존성 주입은 이 원칙을 따르는 방법 중 하나다.
- 쉽게 이야기해서 **구현 클래스에 의존하지 않고 인터페이스에 의존**하라는 뜻  
  - 운전자가 자동차 역할 운전하는것을 알아야지 아반떼 내부구조를 아는건 필요가없음.. 나중에 테슬라로 바꿨을때 자동차의 역할만 알면 문제없음
- 앞에서 이야기한 **역할(Role)에 의존하게 해야한다는 것과 같다.**
- 객체 세상도 클라이언트가 인터페이스에 의존해야 **유연하게 구현체를 변경**할 수 있다!
- **구현체에 의존하게 되면 변경이 아주 어려워진다.**
- 그런데 OCP에서 설명한 MemberService는 인터페이스에 의존하지만, 구현 클래스도 동시에 의존한다.
- MemberService 클라이언트가 구현 클래스를 직접 선택
  - `MemberRepository m = new MemoryMemberRepository();`
- **DIP 위반**





### 코드 변경 이유...!

변경전

```java
public class MemberService {
    private MemberRepository memberRepository = new MemoryMemberRepository();
}
```

변경후

```java
public class MemberService {
    //private MemberRepository memberRepository = new MemoryMemberRepository();
    private MemberRepository memberRepository = new JdbcMemberRepository();
}
```

지금 MemberService 는 MemberRepository 필드를 가지고 있음 근데 new MemoryMemberRepository 를 할당함 그러면 MemberService는 MemoryMemberRepository 에도 의존하고있음(저 코드를 알고있음)

한마디로 MemberService는 MemberRepository 인터페이스뿐만 아니라 MemoryMemberRepository 구현체까지 알고있음 그래서 이걸 다른걸로 바꾸려할때 코드를 수정해야하는 사태가 발생함



MemberService 클라이언트가 구현 클래스를 직접 선택해서 문제 발생

- `MemberRepository m = new MemoryMemberRepository();`

이건 DIP위반.. 추상화에 의존해야지 구체화에 의존하면 안되는데 둘다 의존함



## ✨정리

- 객체 지향의 핵심은 **다형성**
- 다형성만으로는 쉽게 부품을 갈아 끼우듯이 개발할 수 없다. (변경이 용이하지 않다.)
- 다형성 만으로는 구현 객체를 변경할 때 클라이언트 코드도 같이 변경된다.
- 🚨**다형성만으로는 OCP, DIP를 지킬 수 없다.**
- 뭔가 더 필요하다!!

