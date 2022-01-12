# @PostMapping 회원가입 필수값 미입력시 에러메세지 출력



회원가입을 할때 필수값 미입력시 현재페이지에서 에러메세지가 아래와 같이 나타나게 하려면 

- @NotEmpty

- @Valid

- BindingResult result 

을 이용해야 합니다.

### 목표



![](C:\Users\1213h\AppData\Roaming\Typora\typora-user-images\image-20220112155821726.png)

Member Form

```java
@Getter @Setter
public class MemberForm {

    @NotEmpty(message = "회원 이름은 필수 입니다")
    private String name;

    private String city;
    private String street;
    private String zipcode;
}
```

먼저 Member Form에 `@NotEmpty`를 통해 해당 변수가 는 `null` 과 `""` 이라면 허용하지 않고 메세지를 띄웁니다. 참고로  `" "` 은 허용이 됩니다.



> 만약`""`처럼 비어있는 값을 허용하고 싶다면  `@NotNull` 을 사용해 `Null만` 허용하지 않는다는 조건을 붙여주시면 됩니다.
>
> 또한`null` 과 `""` 과 `" "` 모두 허용하고 싶지 않다면  `@NotBlank` 를 사용하시면 됩니다.



MemberController

```java
@PostMapping(value = "/members/new")
public String create(@Valid MemberForm form, BindingResult result) {
    if (result.hasErrors()) {
        return "members/createMemberForm";
    }
    Address address = new Address(form.getCity(), form.getStreet(),
            form.getZipcode());
    Member member = new Member();
    member.setName(form.getName());
    member.setAddress(address);
    memberService.join(member);
    return "redirect:/";
}
```

`@PostMapping(value = "/members/new")` post방식으로 받아오고

`@Valid MemberForm form `  @Valid 가 선언되어있다면 MemberForm클래스의 제약조건에 따라 데이터 유효성을 검사합니다. 데이터가 유효하지 않은 속성이 있다면 이에 대한 에러 정보를 담습니다.

`BindingResult result`  @Valid MemberForm form 에서 오류가 발생하면 원래는 코드가 넘어가지않고 튕겨버리지만 `BindingResult result` 가 있으면 여기에 오류가 담겨서 안에 코드로 넘어갈수 있게 됩니다. 

```java
if (result.hasErrors()) {
        return "members/createMemberForm";
    }
```

result.hasErrors() 를 통해 오류가 발생했을때 다시 createMemberForm 으로 보내면서 에러메세지가 발생합니다.



```
<style>
.fieldError {
    border-color: #bd2130;
}
</style>
```


```
<form role="form" action="/members/new" th:object="${memberForm}" method="post">
    <div class="form-group">
        <label th:for="name">이름</label>
        <input type="text" th:field="*{name}" class="form-control" placeholder="이름을 입력하세요"
               th:class="${#fields.hasErrors('name')}? 'form-control  fieldError' : 'form-control'">
        <p th:if="${#fields.hasErrors('name')}" th:errors="*{name}">Incorrect date</p>
    </div>
</form>
```

`th:class="${#fields.hasErrors('name')}? 'form-control  fieldError' : 'form-control'"`  오류가 발생한다며 해당 영역의 테두리를 빨간색으로 칠한다

`th:if="${#fields.hasErrors('name')}` name과 관련된 에러가 발생했을때 

`th:errors="*{name}"` name과 관련된 에러메세지를 출력한다.

