# Setter 사용을 왜 지양해야할까

> 프로젝트를 진행하면서 Lombok의 도움을 받아 Getter, Setter 어노테이션을 사용했습니다. 하지만 실무에서는 Setter를 남발하며 사용하면 안된다고 합니다. setter를 사용하면 안되는 이유를 공부하며 정리하려 합니다.



## 사용하면 안되는 이유

- Setter 메소드를 사용하면 값을 변경한 의도를 파악하기 힘듭니다.

- 객체의 일관성을 유지하기 어렵습니다.



### 1. Setter 메소드를 사용하면 값을 변경한 의도를 파악하기 힘듭니다.



```java
public Member updateMember(long id, MemberDto) {
    final Member member = findById(id);
    member.setFistName("value");
    member.setLastName("value");
    return member;
}
```

위의 코드는 Member의 정보를 변경하는 코드로 setter 메소드들이 나열돼있습니다. 하지만 위의 코드처럼 setter를 나열한 것만으로 어떤 의도로 데이터를 변경하는지 명확히 알 수 없습니다.



### 2. **객체의 일관성을 유지하기 어렵습니다.**

자바 빈 규약을 따르는 Setter는 public으로 언제든지 변경할 수 있는 상태가 됩니다. 위처럼 회원 변경 메소드뿐만아니라 모든 곳에서 회원의 이름을 변경할수 있는 상태가 되기 때문에 객체의 일관성을 유지하기 어렵습니다.





## Setter 대신 다른 것을 사용하자

- 생성자를 오버로딩
- Builder 패턴 사용
- 정적 팩토리 메소드



**생성자 오버로딩**

```java
public class Member {
    private String name;
    private String age;
    
    public Member(String name){
    	this.name = name;
    }
    public Member(String name,int age){
    	this.name = name;
    	this.age = age;
    }
```

이렇게 생성자를 오버로딩하는 방법도 있지만 멤버변수가 많고 다양한 생성자를 가져야 한다면 코드가 길어지고 가독성이 떨어지는 일이 발생합니다. 이를 해결하기 위해 Builder 패턴을 사용합니다.



**Builder 패턴**

```java
public class Member {
    private String name;
    private String age;
    
    @Builder
    public Member(String name, int age){
    	this.name = name;
        this.age = age;
    }
```

빌더패턴은 요구사항에 맞게 필요한 데이터만 이용하여 유연한 클래스 생성이 가능합니다. 때문에 다양한 생성자들이 사라지고 전체 생성자 하나만을 가지고 있는 형태로 변경되어 유지보수 및 가독성이 향상됩니다.



**정적 팩토리 메소드**

```java
public class Member {
    private String name;
    private String age;
    
    public static createMember(String name, int age){
    	this.name = name;
        this.age = age;
    }
    public static createMemberName(String name){
    	this.name = name;
    }
```

정적 팩토리 메서드를 사용한다면 이름을 가질 수 있기 때문에 반환될 데이터를 추측할 수 있습니다.