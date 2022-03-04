# Optional은 왜 사용하는지 + 사용시 주의사항



Optional은  **NPE 발생을 방지**하기 위해 사용합니다. 

메서드가 반환할 결과값이 ‘없음’을 명백하게 표현할 필요가 있고, `null`을 반환하면 에러를 유발할 가능성이 높은 상황에서 메서드의 **반환 타입**으로 `Optional`을 사용하자는 것이 `Optional`을 만든 주된 목적입니다.



예를 들어  다음과 같은 레파지토리가 존재하고 회원의 이름을 얻기위해 데이터를 가져온다고 가정하겠습니다.

```java
public class MemberRepository {

  Member findByName(String name);

}
```

 다음과 같은 코드에서 findByName()을 호출하는데, Repository(DB)에 **인자로 넘긴 이름(홍길동)의 회원 객체가 없을 경우(NULL)**

`Member member1 = memberRepository.findByName("홍길동");`

member1.getAge(); <<-- **NPE 발생(NullpointerException)**

NPE이 발생합니다.

 

이러한 문제를 해결하기 위해 반환타입을 옵셔널로 적용하여 다음과 같이 사용합니다.

 ```java
 public class MemberRepository {
 
  Optional <Member> findByName(String name);
  
 }
 ```





## 반환타입에만 사용하는 이유



Optional은 데이터를 직렬화할 수 없기때문에 필드로 사용할수 없습니다.



#### **도메인 모델에 Optional을 사용했을 때 데이터를 직렬화할 수 없는 이유**

자바 언어 아키텍트인 브라이언 고츠는 Optional의 용도가 선택형 반환값을 지원하는 것이라고 명확하게 못박았습니다. Optional 클래스는 필드 형식으로 사용할 것을 가정하지 않았으므로 Serializable 인터페이스를 구현하지 않기 때문에 도메인 모델에 Optional을 사용한다면 직렬화(serializable) 모델을 사용하는 도구나 프레임워크에서 문제가 생길 수 있습니다.

```java
// 안 좋음
public class Member {
    private Long id;
    private String name;
    private Optional<String> email = Optional.empty();
}

// 좋음
public class Member {
    private Long id;
    private String name;
    private String email;
}
```



만약 객체 그래프에서 일부 또는 전체 객체가 null일 수 있는 상황이라 Optional을 사용해서 도메인 모델을 구성해야하고 직렬화 모델이 필요하다면 다음 예제에서 보여주는 것처럼 Optional로 값을 반환받을 수 있는 메서드를 추가하는 방식을 권장합니다.

```java
public class Person {
    private Car car;
    public Optional<Car> getCarAsOptional() {
        return Optional.ofNullable(car);
    }
}
```





## `Optional`을 생성자나 메서드 인자로 사용 금지

`Optional`을 생성자나 메서드 인자로 사용하면, 호출할 때마다 `Optional`을 생성해서 인자로 전달해줘야 합니다. 하지만 호출되는 쪽, 즉 api나 라이브러리 메서드에서는 인자가 `Optional`이든 아니든 `null` 체크를 하는 것이 언제나 안전합니다. 따라서 굳이 비싼 `Optional`을 인자로 사용하지 말고 호출되는 쪽에 `null` 체크 책임만 남겨두는 것이 좋다.

```java
// 안 좋음
public class HRManager {
    
    public void increaseSalary(Optional<Member> member) {
        member.ifPresent(member -> member.increaseSalary(10));
    }
}
hrManager.increaseSalary(Optional.ofNullable(member));

// 좋음
public class HRManager {
    
    public void increaseSalary(Member member) {
        if (member != null) {
            member.increaseSalary(10);
        }
    }
}
hrManager.increaseSalary(member);
```







## [이외의 주의사항](https://homoefficio.github.io/2019/10/03/Java-Optional-%EB%B0%94%EB%A5%B4%EA%B2%8C-%EC%93%B0%EA%B8%B0/)

1. `isPresent()-get()` 대신 `orElse()/orElseGet()/orElseThrow()`
2. `orElse(new ...)` 대신 `orElseGet(() -> new ...)`
3. 단지 값을 얻을 목적이라면 `Optional` 대신 `null` 비교
4. `Optional` 대신 비어있는 컬렉션 반환
5. `Optional`을 컬렉션의 원소로 사용 금지
6. `of()`, `ofNullable()` 혼동 주의
7. `Optional<T>` 대신 `OptionalInt`, `OptionalLong`, `OptionalDouble`









> https://www.inflearn.com/questions/376715
>
> https://homoefficio.github.io/2019/10/03/Java-Optional-%EB%B0%94%EB%A5%B4%EA%B2%8C-%EC%93%B0%EA%B8%B0/
>
> https://johngrib.github.io/wiki/java-optional/