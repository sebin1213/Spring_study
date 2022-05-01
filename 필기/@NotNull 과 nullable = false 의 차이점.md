**@NotNull** 과 **nullable = false** 의 차이점

 **nullable = false** 는 DDL에서 not null 에서 제약조건을 걸어준다.

**@NotNull** 또한 DDL에서 not null 에서 제약조건을 걸어준다 추가적으로 유효성 검사까지 진행하며 nullable = false 보다 안전하게 사용할 수 있다