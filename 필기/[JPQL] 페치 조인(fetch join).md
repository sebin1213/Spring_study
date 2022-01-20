## 📌 JPQL - 페치 조인(fetch join)

- SQL 조인 종류X
- JPQL에서 성능 최적화를 위해 제공하는 기능
- **연관된 엔티티나 컬렉션을 SQL 한 번에 함께 조회하는 기능**
- join fetch 명령어 사용
- 페치 조인 ::= [ LEFT [OUTER] | INNER ] JOIN FETCH 조인경로

🔎 회원을 조회하면서 연관된 팀도 함께 조회(SQL 한 번에)
**[JPQL]** select m from Member m join fetch m.team
**[SQL]** SELECT M.*, T.* FROM MEMBER M INNER JOIN TEAM T ON M.TEAM_ID=T.ID

👉🏻 SQL을 보면 회원 뿐만 아니라 팀(T.*)도 함께 SELECT !!
👉🏻즉시 로딩과 비슷하지만, 쿼리로 내가 원하는데로 어떤 객체 그래프를 한번에 조회할지 직접 명시적으로, 동적인 타임에 정할 수 있는게 패치 조인이다.

![img](https://media.vlpt.us/images/uuuuu_j_/post/0c3753a9-923e-4d30-9314-6cb2e25c4f72/%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202022-01-15%20%EC%98%A4%ED%9B%84%206.03.39.png)

```java
String jpql = "select m from Member m join fetch m.team"; 
List<Member> members = em.createQuery(jpql, Member.class) 
> 	.getResultList(); 
      
for (Member member : members) {
	//페치 조인으로 회원과 팀을 함께 조회해서 지연 로딩X 
	System.out.println("username = " + member.getUsername() + ", " + "teamName = " + member.getTeam().name()); 
}
```

‼️ **지연로딩으로 설정해도 페치 조인이 항상 우선이다.**

🔎 일대다 관계, 컬렉션 페치 조인 시 주의사항!

```java
String jpql = "select t from Team t join fetch t.members where t.name = '팀A'" 
List<Team> teams = em.createQuery(jpql, Team.class).getResultList(); 

for(Team team : teams) { 
	System.out.println("teamname = " + team.getName() + ", team = " + team); 
}
    
for (Member member : team.getMembers()) { 
	//페치 조인으로 팀과 회원을 함께 조회해서 지연 로딩 발생 안함 
	System.out.println(“-> username = " + member.getUsername()+ ", member = " + member); 
} 
```

🔎 일대다(@OneToMany) + fetch join 시 뻥튀기 발생!
[결과]
teamname = 팀A, team = Team@0x100 
-> username = 회원1, member = Member@0x200
-> username = 회원2, member = Member@0x300
teamname = 팀A, team = Team@0x100 
-> username = 회원1, member = Member@0x200
-> username = 회원2, member = Member@0x300

![img](https://media.vlpt.us/images/uuuuu_j_/post/f8994c27-594d-45d1-8acf-1167275ccead/%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202022-01-15%20%EC%98%A4%ED%9B%84%206.09.19.png)
팀과 멤버를 fetch join해서 나온 결과는 [TEAM JOIN MEMBER] 테이블과 같다. 이 테이블이 JPA에게 넘겨지면, JPA는 pk 값이 같더라도 row 수 만큼 만들어야 하기 때문에, 결과적으로 다음과 같은 그림이 그려진다.
![img](https://media.vlpt.us/images/uuuuu_j_/post/1c02cbc7-48b8-4061-86a8-6ee8f5749d46/%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202022-01-15%20%EC%98%A4%ED%9B%84%206.09.51.png)

🔎 **해결 방안**

같은 식별자를 가지는 동일한 TeamA가 두번 만들어지는 것이 문제이기 때문에 중복된 TeamA를 삭제하면 된다!
이를 해결하기 위해 JPQL의 DISTINCT는 2가지 기능을 제공한다.
\1. SQL에 DISTINCT를 추가
select distinct t from Team t join fetch t.members where t.name = ‘팀A’
**완전히 똑같은 데이터야 distinct가 된다.**
![img](https://media.vlpt.us/images/uuuuu_j_/post/db9b439a-cf5b-4a57-98a1-4869e34063c7/%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202022-01-15%20%EC%98%A4%ED%9B%84%206.10.27.png)
SQL에 DISTINCT를 추가하지만 데이터가 다르므로 SQL 결과에서 중복제거가 실패한다.

1. 애플리케이션에서 엔티티 중복 제거
   JPQL의 DISTINCT가 추가로 애플리케이션에서 중복 제거를 시도한다.
   **같은 식별자를 가진 Team 엔티티를 제거한다.**
   ![img](https://media.vlpt.us/images/uuuuu_j_/post/084ca8aa-8463-45bd-888e-a328fb427a87/%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202022-01-15%20%EC%98%A4%ED%9B%84%206.11.17.png)

[DISTINCT 추가시 결과] 
teamname = 팀A, team = Team@0x100 
-> username = 회원1, member = Member@0x200
-> username = 회원2, member = Member@0x300





> https://velog.io/@uuuuu_j_/%EC%9E%90%EB%B0%94-ORM-%ED%91%9C%EC%A4%80-JPA-%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%B0%8D-%EA%B8%B0%EB%B3%B8%ED%8E%B8-13