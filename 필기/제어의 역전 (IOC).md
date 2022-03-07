# 제어의 역전 (IOC)

https://mangkyu.tistory.com/150

https://mangkyu.tistory.com/125



제어가 뒤바뀌었다는 제어의 역전...? 대체 제어의 역전을 무엇일까 





제어의 역전 없이 코드를 만든다면 다음과 같이 본인이 직접 new를 통해 의존성을 가져오는 방식을 사용할 수 있습니다.

```java
class MemberService{
	private MemberRepository repository = new MemberRepository();
}
```

하지만 이렇게 의존성을 가져온다면 MemberService가 MemberRepository에 의존하게 되어버립니다.  MemberRepository가 존재하지 않는다면 MemberService를 제대로 사용할 수 없게 됩게 됩니다.  



때문에 이런 의존성을 없애기 위해 의존성을 자신이 관리하는게 아니라 역전시켜 자신 이외의 누군가 밖에서 의존성을 넣어주는 방식을 사용하고 이를 제어의 역전(loc)라고 합니다.

```java
class MemberService{
	private MemberRepository repository;
	
	public MemberService(MemberRepository repository){
		this.repository = repository;
	}
}
```

이제 MemberService는 MemberRepository 타입의 변수만 들고있고 그리고 누군가가 생성자를 통해 의존성을 준다라고 가정을 하고 repository를 사용하면 됩니다.



의존성 주입은 아래와 같은 형태로 나타낼수 있습니다.

```java
class MemberServiceTest	{
	@Test
	public void create(){
		MemberRepository repository = new MemberRepository();
		MemberService service = new MemberService(repository);
	}
}
```

MemberRepository를 만들어서 생성자를 통해 MemberService(repository) 이렇게 넘겨주면 됩니다.





# IOC 컨테이너

스프링 프레임워크에서는 IOC를 위한 컨테이너를 제공합니다. 이 컨테이너에서 가장 핵심적인건 ApplicationContext 와 BeanFactory입니다.

ApplicationContext 라는 인터페이스

인터페이스가 ioc 컨테이너라고 부름

