package com.example.demo.service;

import com.example.demo.domain.Member;
import com.example.demo.repository.MemberRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
// 회원 저장소를 활용해 실제 비지니스 로직을 작성하는 부분
// 서비스를 통해 member 가입
// 비즈니스 이름에 가까운 이름을사용해야함

// @Service // 컨테이너에서 MemberService 를 인식할수 있도록 넣어줌 MemberController 랑 연결된...
@Transactional
public class MemberService {

//    private final MemberRepository memberRepository = new MemoryMemberRepository();
    private final MemberRepository memberRepository;
    // new로 직접 생성하는게 아니라 외부에서 넣어주도록 바꿈
//    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    /**
     * 회원가입
     */
//    public Long join(Member member) { // 중복이름 검출
//        Optional<Member> result = memberRepository.findByName(member.getName());
//        result.getorElseGet() // 함수 많이 사용 값이 있으면 꺼내고 없으면 메서드를 실행
//        result.ifPresent(m -> {
//                    throw new IllegalStateException("이미 존재하는 회원입니다.");});
//    }
    public Long join(Member member) {
        validateDuplicateMember(member); //같은 이름의 중복 회원 검증
        memberRepository.save(member);
        return member.getId(); // 통과하면 저장
    }
    // 로직이 그냥 쭉 나오니까 메서드로 만들자
    // 중복 회원 검증 메서드
    private void validateDuplicateMember(Member member) {
        memberRepository.findByName(member.getName())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }
    /**
     * 전체 회원 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }
    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }
}