## 롬복

막상 개발을 해보면, 대부분이 다 불변이고, 그래서 다음과 같이 생성자에 final 키워드를 사용하게 된다. 그런데 생성자도 만들어야 하고, 주입 받은 값을 대입하는 코드도 만들어야 하고…코드가 너무 길다..!!
**필드 주입처럼 좀 편리하게 사용하는 방법**은 없을까? 하고 나온게 롬복이라는 라이브러리다.

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

- 롬복 라이브러리가 제공하는 **@RequiredArgsConstructor** 기능을 사용하면 final이 붙은 필드를 모아서 생성자를 자동으로 만들어준다. (다음 코드에는 보이지 않지만 실제 호출 가능하다.)

  

```java
@Component
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final MemberRepository memberRepository;
	private final DiscountPolicy discountPolicy;
}
```

매우 간단해졌다.!

- @RequiredArgsConstructor 필수값인 final 이 붙은거를 파라미터로 받는 생성자를 만들어준다.
- 최근에 많이 사용함!

> **정리**
> 최근에는 생성자를 딱 1개 두고, @Autowired 를 생략하는 방법을 주로 사용한다. 여기에 Lombok 라이브러리의 @RequiredArgsConstructor 함께 사용하면 기능은 다 제공하면서, 코드는 깔끔하게 사용할 수 있다.





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


1. Preferences(윈도우 File Settings) plugin lombok 검색 설치 실행 (재시작) or start.spring.io에서 dependence에 추가
2. Preferences Annotation Processors 검색 Enable annotation processing 체크 (재시작)
3. 임의의 테스트 클래스를 만들고 @Getter, @Setter 확인

![image-20211123194349548](C:\Users\hope\AppData\Roaming\Typora\typora-user-images\image-20211123194349548.png)









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
\- 1. 타입 매칭
\- 2. 타입 매칭의 결과가 2개 이상일 때 의존성 주입받는 필드 명으로 빈 이름 매칭

#### 😙 @Qualifier 사용

`@Qualifier`는 **추가 구분자**를 붙여주는 방법이다. 주입시 추가적인 방법을 제공하는 것이지 빈 **이름을 변경하는 것은 아니다.**

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

`@Qualifier`로 주입할 때, `@Qualifier("mainDiscountPolicy")`를 못찾으면 어떻게 될까? 그러면 mainDiscountPolicy라는 이름의 **스프링 빈을 추가로 찾는다**. 하지만 경험상 `@Qualifier`는 `@Qualifier`를 찾는 용도로만 사용하는 것이 명확하고 좋다.

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







## 애노테이션 직접 만들기

@Qualifier("mainDiscountPolicy") 이렇게 문자를 적으면 컴파일시 타입 체크가 안된다. 다음과 같은
애노테이션을 만들어서 문제를 해결할 수 있다.

```java
package hello.core.annotataion;
import org.springframework.beans.factory.annotation.Qualifier;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Qualifier("mainDiscountPolicy")
public @interface MainDiscountPolicy {
}
```



```java
@Component
@MainDiscountPolicy
public class RateDiscountPolicy implements DiscountPolicy {}
```



```java
//생성자 자동 주입
@Autowired
public OrderServiceImpl(MemberRepository memberRepository,
@MainDiscountPolicy DiscountPolicy discountPolicy) {
    this.memberRepository = memberRepository;
	this.discountPolicy = discountPolicy;
}

//수정자 자동 주입
@Autowired
public DiscountPolicy setDiscountPolicy(@MainDiscountPolicy DiscountPolicy
discountPolicy) {
	return discountPolicy;
}
```

>애노테이션에는 상속이라는 개념이 없다. 이렇게 여러 애노테이션을 모아서 사용하는 기능은 스프링이
>지원해주는 기능이다. @Qulifier 뿐만 아니라 다른 애노테이션들도 함께 조합해서 사용할 수 있다.
>단적으로 @Autowired도 재정의 할 수 있다. 물론 스프링이 제공하는 기능을 뚜렷한 목적 없이 무분별하게
>재정의 하는 것은 유지보수에 더 혼란만 가중할 수 있다.