## 빈 스코프란?

지금까지 우리는 **스프링 빈이 스프링 컨테이너의 시작과 함께 생성되어서 스프링 컨테이너가 종료될 때 까지 유지**된다고 학습했다.
이것은 스프링 빈이 기본적으로 **싱글톤 스코프**로 생성되기 때문이다.

스코프는 번역 그대로 **빈이 존재할 수 있는 범위**를 뜻한다.

**스프링 빈은 다음과 같은 다양한 스코프를 지원한다.**

- 싱글톤 : 기본 스코프, 스프링 컨테이너의 시작과 종료까지 유지되는 가장 넓은 범위의 스코프이다.
- 프로토타입 : 스프링 컨테이너는 프로토타입 빈의 생성과 의존관계 주입까지만 관여하고 더는 관리하지 않는 매우 짧은 범위의 스코프이다.
- 웹 관련 스코프
  - request : 웹 요청이 들어오고 나갈 때 까지 유지되는 스코프
  - session : 웹 세션이 생성되고 종료될 때 까지 유지되는 스코프
  - application : 웹의 서블릿 컨텍스트와 같은 범위로 유지되는 스코프

빈 스코프는 다음과 같이 지정할 수 있다.

```java
//컴포넌트 스캔 자동 등록
@Scope("prototype")
@Component
public class HelloBean {}

//자바 설정파일(@Configuration 파일) 수동등록
@Scope("prototype")
@Bean
PrototypeBean HelloBean() {
    return new HelloBean();
}
```

## 프로토타입 스코프

싱글톤 스코프의 빈을 조회하면 스프링 컨테이너는 항상 같은 인스턴스의 빈을 반환한다.

반면에 프로토타입 스코프를 스프링 컨테이너에 조회하면 스프링 컨테이너는 항상 새로운 인스턴스를 생성해서 반환한다.

#### 👩🏻 싱글톤 빈 요청

![img](https://media.vlpt.us/images/syleemk/post/085143fd-e601-4357-85b3-f17baff9b305/image.png)
\1. 싱글톤 스코프의 빈을 스프링 컨테이너에게 요청한다.
\2. 스프링 컨테이너는 본인이 관리하는 스프링 빈을 반환한다.
\3. 이후에 스프링 컨테이너에 같은 요청이 와도 같은 객체 인스턴스 스프링 빈을 반환한다.

#### 🧑🏻 프로토타입 빈 요청

![img](https://media.vlpt.us/images/syleemk/post/06453d22-0d22-4156-a665-3c0acd1e05f0/image.png)
\1. 프로토타입 스코프의 빈을 스프링 컨테이너에 요청한다.
\2. 스프링 컨테이너는 **이 시점에 프로토타입 빈을 생성**하고, 필요한 의존관계를 주입한다.

싱글톤 빈은 컨테이너 생성시점에 같이 생성되고 초기화 되지만, 프로토타입 빈은 스프링 컨테이너에서 **빈을 조회할 때** 생성되고 초기화 메서드도 실행된다.

![img](https://media.vlpt.us/images/syleemk/post/d51c0d8c-7599-4d10-87dd-4a66537285c5/image.png)
\3. 스프링 컨테이너는 생성한 프로토타입 빈을 클라이언트에 반환한다.
\4. 이후에 스프링 컨테이너에 같은 요청이 오면 항상 새로운 프로토타입 빈을 생성해서 반환한다.

> 여기서 핵심은 **스프링 컨테이너는 프로토타입 빈을 생성하고, 의존관계 주입, 초기화까지만 처리한다는 것이다.**

클라이언트에게 빈을 반환하고, 이후 스프링 컨테이너는 생성된 프로토타입 빈을 관리하지 않는다. 프로토타입 빈을 관리할 책임은 프로토타입 빈을 받은 클라이언트에게 있다. 그래서 `@PreDestroy`같은 종료 콜백 메서드가 호출되지 않는다.

#### 👶🏻 프로토타입 빈의 특징 정리

- 스프링 컨테이너에 요청할 때마다 새로 생성된다.
- 스프링 컨테이너는 프로토타입 빈의 생성과 의존관계 주입 그리고 초기화까지만 관여한다.
- 종료 메서드가 호출되지 않는다.
- 그래서 프로토타입 빈은 프로토타입 빈을 조회한 클라이언트가 관리해야한다. 종료 메서드에 대한 호출도 클라이언트가 직접 해야한다.

### 싱글톤 빈과 함께 사용시 문제점

스프링 컨테이너에 프로토타입 스코프의 빈을 요청하면 항상 새로운 객체 인스턴스를 반환한다. 하지만 싱글톤 빈과 함께 사용할 때는 의도한 대로 잘 동작하지 않으므로 주의해야한다.

#### 🌟 프로토타입 빈 직접 요청

![img](https://media.vlpt.us/images/syleemk/post/292e5d76-adf3-4513-8a0a-480e3a2fb39f/image.png)
\1. 클라이언트 A는 스프링 컨테이너에 프로토타입 빈을 직접 요청한다.
\2. 스프링 컨테이너는 요청을 받고, 프로토타입 빈을 새로 생성해서 반환(x01)한다. 해당 빈의 count필드 값은 0이다.
\3. 클라이언트는 조회한 프로토타입 빈에 `addCount()`를 호출하면서 count 필드를 +1 한다.
\4. 결과적으로 프로토타입 빈(x01)의 count는 1이 된다.
![img](https://media.vlpt.us/images/syleemk/post/6be7fd72-d87f-496d-86ff-91b5c651fd4d/image.png)
\1. 클라이언트 B는 스프링 컨테이너에 프로토타입 빈을 요청한다.
\2. 스프링 컨테이너는 프로토타입 빈을 **새로 생성**해서 반환(x02)한다. 해당 빈의 count필드 값은 0이다.
\3. 클라이언트는 조회한 프로토타입 빈에 `addCount()`를 호출하면서 count필드를 +1한다.
\4. 결과적으로 빈(x02)의 count는 1이된다.

> 프로토타입 빈을 직접 요청하는 경우 의도한 대로 동작한다.

#### 🌟 싱글톤에서 프로토타입 빈 사용

![img](https://media.vlpt.us/images/syleemk/post/685561ab-b59f-4652-850f-27f7a090cc3d/image.png)

- `clientBean`은 싱글톤이므로, 보통 스프링 컨테이너 생성 시점에 함께 생성되고, 의존관계 주입도 발생한다.

1. `clientBean`은 의존관계 자동 주입을 사용한다. 주입 시점에 스프링 컨테이너에게 프로토타입 빈을 요청한다.
2. 스프링 컨테이너는 프로토타입 빈에 대한 요청을 받고, 프로토타입 빈을 생성해서 `clientBean`에게 반환한다. 프로토타입 빈의 count필드 값은 0이다.

- 이제 `clientBean`은 프로토타입 빈을 내부 필드에 보관한다. (정확히는 참조값을 보관한다.)
  ![img](https://media.vlpt.us/images/syleemk/post/459773f6-8a22-49c9-aa37-fdcbeaf73766/image.png)
- 클라이언트 A는 `clientBean`을 스프링 컨테이너에 요청해서 받는다. 싱글톤이므로 항상 같은 `clientBean`이 반환된다.

1. 클라이언트 A는 `clientBean.logic()`을 호출한다.
2. `clientBean`은 prototypeBean의 `addCount()`를 호출해서 프로토타입 빈의 count를 증가시킨다. count값이 1이된다.
   ![img](https://media.vlpt.us/images/syleemk/post/6916b407-4847-4d83-bee2-b849f744814d/image.png)

- 클라이언트 B는 `clientBean`을 스프링 컨테이너에 요청해서 받는다. 싱글톤이므로 항상 같은 `clientBean`이 반환된다.
- 여기서 중요한 점은, `clientBean`이 내부에 가지고 있는 프로토타입 빈은 이미 과거에 **주입이 끝난 빈**이다. **주입 시점**에 스프링 컨테이너에 **요청**해서 프로토타입 빈이 새로 **생성**된 것이지, **사용할 때 마다 새로 생성되는 것이 아니다!**

1. 클라이언트 B는 `clientBean.logic()`을 호출한다.
2. `clientBean`은 prototypeBean의 `addCount()`를 호출해서 프로토타입 빈의 count를 증가시킨다. 원래 count값이 1이었으므로 2가된다.

> 싱글톤과 프로토타입을 함께 사용하는 경우 기대한 대로 동작하지 않는다.
> (각 요청마다 count 값을 1로 반환받는 것을 기대했지만 싱글톤 생성시 의존관계 주입받은 프로토타입 빈을 그대로 사용하게 되어버려서 2를 반환받게된다.)

스프링은 일반적으로 싱글톤 빈을 사용하므로, **싱글톤 빈이 프로토타입 빈을 사용**하게 된다.

그런데 싱글톤 빈은 **생성 시점에만 의존관계를 주입 받는다.**

따라서 스프링 싱글톤 빈이 생성되는 시점에 프로토 타입 빈도 새로 생성되어서 주입되긴 하지만, 싱글톤 빈과 함게 **계속 유지되는 것이 문제**다.

> 원하는 것은 프로토타입 빈을 주입 시점에만 새로 생성하는 것이 아니라, 사용할 때마다 새로 생성해서 사용하는 것이다.

### Provider로 문제 해결

어떻게 하면 싱글톤 빈과 프로토타입 빈을 같이 사용할 때, 프로토 타입 빈을 사용할 때마다 항상 새로운 프로토타입 빈을 생성할 수 있을까?

#### 🤔 스프링 컨테이너에 요청

가장 간단한 방법은 싱글톤 빈이 프로토타입을 사용할 때마다 스프링 컨테이너에 새로 요청하는 것이다.

```java
@Component
class ClientBean {
    
    //ClientBean에서 applicationContext 자체를 주입받아서 직접 요청한다.
    @Autowired
    private ApplicationContext ac;
    
    public int logic() {
        PrototypeBean prototypeBean = ac.getBean(PrototypeBean.class);
        prototypeBean.addCount();
        return prototypeBean.getCount();
    }
}

@Component
@Scope("prototype")
class PrototypeBean {
    
    private int count = 0;
    
    public void addCount() {
        count++;
    }
    
    public int getCount() {
        return count;
    }
    
    @PostConstruct
    public void init() {
        System.out.println("PrototypeBean.init" + this);
    }
    
    @PreDestroy
    public void destroy() {
        System.out.println("PrototypeBean.destroy");
    }
}
```

- 실행시 `ac.getBean()`을 통해서 항상 새로운 프로토타입 빈이 생성된다.

> 의존관계를 외부에서 주입(DI)받는 것이 아니라 이렇게 직접 필요한 의존관계를 찾는 것을 **Dependency Lookup(DL)** 의존관계 조회(탐색)이라고 한다.

- 그런데 이렇게 스프링 애플리케이션 컨텍스트 전체를 주입받게 되면, **스프링 컨테이너에 종속적인 코드가되고, 단위 테스트도 어려워진다.**
- 지금 필요한 기능은 지정한 프로토타입 빈을 컨테이너에서 대신 찾아주는, 딱! **DL**정도의 기능만 제공하는 무언가가 있으면 된다.

#### 🤩 ObjectFactory, ObjectProvider

지정한 빈을 컨테이너에서 대신 찾아주는 DL(Dependency Lookup)서비스를 제공하는 것이 바로 `ObjectProvider`이다.

참고로 과거에는 `ObjectFactory`가 있었는데, 여기에 편의 기능을 추가해서 `ObjectProvider`가 만들어졌다.

스프링 애플리케이션 컨텍스트 전체를 주입받는 대신, `ObjectProvider`를 주입받아서 사용한다.

```java
@Autowired
private ObjectProvider<PrototypeBean> prototypeBeanProvider;

public int logic() {
    PrototypeBean prototypeBean = prototypeBeanProvider.getObject();
    prototypeBean.addCount();
    int count = prototypeBean.getCount();
    return count;
}
```

- 실행하면 `prototypeBeanProvider.getObject()`를 통해서 항상 새로운 프로토타입 빈이 생성되는 것을 알 수 있다.
- **`ObjectProvider`의 `getObject()`를 호출하면 내부에서는 스프링 컨테이너를 통해 해당 빈을 찾아서 반환한다.(DL)**
- 스프링이 제공하는 기능을 사용하지만, 기능이 단순하므로 단위 테스트를 만들거나 mock 코드를 만들기는 훨씬 쉬워진다.
- 스프링에 의존하지만 별도 라이브러리는 필요없다.
- `ObjectProvider`는 지금 딱 필요한 DL정도의 기능만 제공한다.

### 정리

- 프로토타입 빈은 매번 사용할 때마다 의존관계 주입이 완료된 새로운 객체가 필요할 때 사용하면 된다.
- 실무에서 웹 애플리케이션을 개발하다보면, 싱글톤 빈으로 대부분의 문제를 해결할 수 있기 때문에 프로토타입 빈을 직접적으로 사용하는 일은 매우 드물다.
- ObjectProvider, JSR330 Provider 등은 프로토타입 뿐만 아니라 DL이 필요한 경우는 언제든지 사용가능하다.

## 웹 스코프

싱글톤 스코프 : 스프링 컨테이너의 시작과 끝까지 함께하는 매우 긴 스코프
프로토타입 스코프 : 생성과 의존관계 주입, 초기화까지만 진행하는 특별한 스코프

### 웹 스코프의 특징

- 웹 스코프는 웹 환경에서만 동작한다.
- 웹 스코프는 프로토타입과 다르게 **스프링이 해당 스코프의 종료시점까지 관리**한다. 따라서 **종료메서드가 호출**된다.

### 웹 스코프 종류

- request : HTTP 요청 하나가 들어오고 나갈 때까지 유지되는 스코프, 각각의 HTTP 요청마다 별도의 빈 인스턴스가 생성되고 관리된다.
- session : HTTP Session과 동일한 생명주기를 가지는 스코프
- application : 서블릿 컨텍스트(ServletContext)와 동일한 생명주기를 가지는 스코프
- websocket : 웹 소켓과 동일한 생명주기를 가지는 스코프

### HTTP Request 요청당 각각 할당되는 request 스코프

![img](https://media.vlpt.us/images/syleemk/post/6234e506-953a-4ca5-be0f-253ac6c8d088/image.png)

- 각각의 클라이언트 요청당 하나의 빈 인스턴스가 생성되고 반환된다.

### 스코프와 Provider

스프링 빈 등록시 웹 스코프를 그대로 주입받으면 오류가 발생한다.
싱글톤 빈은 스프링 컨테이너 생성시 함께 생성되어서 라이프 사이클을 같이하지만, 웹 스코프(여기서는 request 스코프)의 경우 HTTP 요청이 올 때 새로 생성되고 응답하면 사라지기 때문에, 싱글톤 빈이 생성되는 시점에는 아직 생성되지 않았다. 따라서 의존관계 주입이 불가능하다.

#### 😉 Provider 사용

- 앞에서 배운 ObjectProvider를 사용하면, `ObjectProvider.getObject()`를 호출하는 시점까지 request scope **빈의 생성을 지연**할 수 있다.
- HTTP 요청이 들어오는 시점에 `ObjectProvider.getObject()`을 호출하고, HTTP 요청이 진행중이므로 (아직 응답이 가지 않음) 해당 요청을 받은 스프링 컨테이너는 request scope 빈을 생성한다.
- `ObjectProvider.getObject()`를 서로 다른 싱글톤 빈에서 각각 호출하더라도 **같은 HTTP요청이면 같은 스프링 빈이 반환된다!!** ➡ 내가 직접 구분하려면 엄청 힘들다 😨

### 스코프와 프록시

Provider 사용보다 더 간단해진 방법이다.

```java
@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_ClASS)
public class MyLogger {
}
```

- ```
  @Scope
  ```

  속성에

   

  ```
  proxyMode = ScopedProxyMode.TARGET_CLASS
  ```

  를 추가해준다

  - 적용대상이 인터페이스가 아닌 클래스면 `TARGET_CLASS`를 선택
  - 적용대상이 인터페이스면 `INTERFACES`를 선택

- **이렇게 하면 MyLogger의 가짜 프록시 클래스를 만들어두고 HTTP request와 상관없이 가짜 프록시 클래스를 다른 빈에 미리 주입해둘 수 있다.**

#### 😮 웹 스코프와 프록시 동작 원리

주입된 웹 스코프 빈을 확인해보자

```java
System.out.println("myLogger = " + myLogger.getClass());
myLogger = class hello.core.common.MyLogger$$EnhancerBySpringCGLIB$$b68b726d
```

**CGLIB라는 라이브러리로 내 클래스를 상속받은 가짜 프록시 객체를 만들어서 주입한다!** (스프링 컨테이너가 싱글톤 빈 등록할때 사용하는 라이브러리와 같다)

- `@Scope`의 `proxyMode = ScopedProxyMode.TARGET_CLASS`를 설정하면 스프링 컨테이너는 CGLIB라는 바이트코드 조작 라이브러리를 사용해서, 해당 웹 스코프 빈 클래스를 상속하는 가짜 프록시 객체를 생성한다.
- 위의 결과를 보면 내가 등록한 순수 자바 클래스 빈이 아니라 가짜 프록시 객체가 등록된 것을 확인할 수 있다.
- 그리고 스프링 컨테이너에 원래 만들었던 진짜 클래스 이름으로 (첫문자 소문자, 빈 이름 규칙) (여기서는 myLogger) 진짜 대신에 이 가짜 프록시 객체를 등록한다.
- `ag.getBean("myLogger", MyLogger.class)`로 조회해도 프록시 객체가 조회된다
- 그래서 의존관계 주입도 이 가짜 프록시 객체가 주입된다.

![img](https://media.vlpt.us/images/syleemk/post/700690df-fc01-4e25-b0ce-96073c4c35d7/image.png)

- **가짜 프록시 객체는 요청이 오면 그때 내부에서 진짜 빈을 요청하는 위임 로직이 들어있다.**
- 가짜 프록시 객체는 내부에 진짜 myLogger를 찾는 방법을 알고있다.
- 클라이언트가 `myLogger.logic()`을 호출하면 사실은 가짜 프록시 객체의 메서드를 호출한 것이다.
- 가짜 프록시 객체는 request 스코프의 진짜 `myLogger.logic()`을 호출한다.
- 가짜 프록시객체는 원본 클래스를 상속받아서 만들어졌기 때문에 이 객체를 사용하는 클라이언트 입장에서는 사실 원본인지 아닌지도 모르게 동일하게 사용할 수 있다 (다형성)

#### 🧐 동작 정리

- CGLIB라는 라이브러리로 내 클래스를 상속받은 가짜 프록시 객체를 만들어서 주입한다.
- 이 가짜 프록시 객체는 **실제 요청이 오면 그때 내부에서 실제 빈을 요청하는 위임 로직**이 들어있다.
- 가짜 프록시 객체는 실제 request scope와는 관계가 없다. **그냥 가짜이고, 내부에 단순한 위임 로직만 있고, 싱글톤처럼 동작**한다.

#### 🤓 특징 정리

- 프록시 객체 덕분에 클라이언트는 마치 **싱글톤 빈을 사용하듯이** 편리하게 request scope를 사용할 수 있다.
- 사실 Provider를 사용하든, 프록시를 사용하든 핵심 아이디어는 진짜 객체 조회를 **꼭 필요한 시점까지 지연 처리**한다는 점이다.
- 단지 애노테이션 설정 변경만으로 원본 객체를 프록시 객체로 대체할 수 있다. 이것이 바로 다형성과 DI컨테이너가 가진 큰 강점이다!
- 꼭 웹 스코프가 아니어도 프록시는 사용할 수 있다!

#### 😨 주의점

- 마치 싱글톤을 사용하는 것 같지만 다르게 동작하기 때문에 결국 주의해서 사용해야한다!
- 이런 특별한 scope는 꼭 필요한 곳에만 최소화해서 사용하자, 무분별하게 사용하면 유지보수하기 어렵다.