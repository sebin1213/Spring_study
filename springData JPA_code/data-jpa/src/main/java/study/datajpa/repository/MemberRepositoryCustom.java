package study.datajpa.repository;

import study.datajpa.entity.Member;

import java.util.List;

// memberRepository 에 존재하는 모든기능을 구현할수는 없으니 정말 커스텀하고싶은기능 따로 인터페이스로 빼서 커스텀
public interface MemberRepositoryCustom {
    List<Member> findMemberCustom();
}
