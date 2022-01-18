# @PostMapping, @RequestBody

```python
@RestController
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }
    @Data
    static class CreateMemberRequest {
        private String name;
    }
    @Data
    static class CreateMemberResponse {
        private Long id;
        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }
}

```
### @RestController
`@RestController`는 @Controller에 @ResponseBody가 추가된 것입니다. 

비동기통신을 하기위해서는 클라이언트에서 서버로 요청 메세지를 보낼 때, 본문에 데이터를 담아서 보내야 하고, 서버에서 클라이언트로 응답을 보낼때에도 본문에 데이터를 담아서 보내야 합니다. 해당 본문을 body라고 부르며 요청본문은 requestBody에, 응답본문은 responseBody에 담아 데이터를 전송합니다.

때문에 RestController는 응답본문에 담긴 Json 형태의 객체 데이터를 반환할때 사용합니다.
>[Json이란](https://zeddios.tistory.com/90)

### @RequiredArgsConstructor
롬복을 사용해 생성자를 주입하기위해 사용한 어노테이션입니다.
롬복을 사용하지 않고 생성자를 주입한다면 아래와 같이 작성해야 합니다.
```python
@Service
public class MemberApiController {
    
    @Autowired
    private MemberService memberService;
    
    public MemberApiController (MemberService memberService) {
                this.memberService = memberService;
            }

```


### @PostMapping("/api/v2/members")
Post 통신을 통해 파라미터를 넘겨받습니다.

### @RequestBody
만약, 받아야하는 데이터가 있다면, @RequestBody를 활용하여 받아옵니다.
@RequestBody는 json형태로 온 HTTP Body의 데이터를 memeber로 매핑해서 바꿔줍니다.

### @Valid
`@Valid Member member`
@Valid 하면 Member엔티티의 유효성을 검증합니다.


### 회원가입
```
Member member = new Member();
member.setName(request.getName());
Long id = memberService.join(member);
return new CreateMemberResponse(id);
```
Member엔티티를 가져오고 request의 이름을 member에 넣어줍니다.
그리고 memberService에서 작성한 회원가입 로직에 넣어줍니다. 

> **Long타입으로 반환하지 않는 이유**
> return id로 반환한다면 json타입이 아닌 단순 아이디 값만 반환이 됩니다.
> Long으로 반환: 10
> CreateMemberResponse로 반환: {"id":10}


### @Data
클래스안의 모든 private 필드에 대해 @Getter와 @Setter를 적용하고 클래스내에 @ToString 과 @EqualsAndHashCode를 적용시켜 메소드를 오버라이드 해주며 @RequiredArgsConstructor를 지정해 줍니다.


### CreateMemberRequest DTO생성

데이터를 전달받을 DTO를 생성합니다.

>CreateMemberRequest는 static으로 선언해야합니다. 
>내부 클래스에 static을 선언하지 않으면 MemberController 클래스 외부에서 해당 객체를 직접 생성할 수 없습니다.
>Response는 클래스 내부에서 생성해서 반환하기 때문에 static이 없어도 되지만, Request는 클래스 외부에서 생성해서 들어오기 때문에 static으로 생성해야 객체를 생성할 수 있습니다.


### CreateMemberResponse

데이터를 반환하는 클래스입니다.
{"id":10} 형태로 데이터를 반환합니다.