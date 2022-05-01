### ✏️ UserDetailsService

- UserDetailService 인터페이스는 데이터베이스에서 회원 정보를 가져오는 역할을 담당합니다.
- loadUserByUsername() 메소드가 존재하며, 회원 정보를 조회하여 사용자의 정보와 권한을 갖는 UserDetails 인터페이스를 반환합니다.
- 인터페이스를 구현하게 되면 Spring Security에서 구현한 클래스를 사용자 정보로 인식하고 인증 작업을 한다.

### ✏️ UserDetail

스프링 시큐리티에서 회원의 정보를 담기 위해서 사용하는 인터페이스는 UserDetails입니다. 이 인터페이스를 직접 구현하거나 스프링 시큐리티에서 제공하는 User클래스를 사용합니다.

