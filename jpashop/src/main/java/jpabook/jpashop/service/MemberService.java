package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepositoryOld;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
//@Transactional 여기에 애노테이션을 넣으면 public 메서드들은 다 적용됨
@Transactional(readOnly = true) //하지만 조회할때만 readOnly줄꺼라서 어노테이션 추가하고 join에도 어노테이션 다로 붙여줌넣어줌
//@AllArgsConstructor //생성자 만들어줌
@RequiredArgsConstructor // final 있는 필드만 가지고 생성자 만들어줌
public class MemberService {

    private final MemberRepositoryOld memberRepository;  // final 붙이면 아래에 생성자 값 안만ㄷ르면 빨간줄뜨면서 체크할수있음

//    @Autowired // 없어도 됨 최근 스프링에서 지원해줌
//    public MemberService(MemberRepository memberRepository){
//        this.memberRepository = memberRepository;
//    }

    /**
     *회원가입
     */
    @Transactional //jpa의 모든 데이터 변경이나 로직은 트랜젝션 안에서 실행되어야함 그래야 지연로딩같은거 등등 할수있음
    public Long join(Member member) {
        validateDuplicateMember(member); //중복 회원 검증
        memberRepository.save(member); //그냥 넘기기만하면 끝 근데 중복은..?
        return member.getId(); //아이디 반환 이렇게 꺼내면 아직 db에 들어간 시점이 아니여도 항상 값이 있다는 보장이 된다..
    }


    /**
     *중복회원이면 터트려 버리기
     */
    private void validateDuplicateMember(Member member){
        // 더 최적화 시키고 싶다면 멤버수를 세고 그 수가 0 이상이면 문제있다고 말하면 됨

        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 회원 수정
     */
    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id);
        member.setName(name);
    }


    //회원 전체 조회
//    @Transactional(readOnly = true) jpa에서 조회할때만 이걸 사용하면 좀더 최적화한 상태로 조회할수 있음
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

}
