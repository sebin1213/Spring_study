// db대신 메모리에서 사용

package start.hello_spring.repository;
import org.springframework.stereotype.Repository;
import start.hello_spring.domain.Member;
import java.util.*;

// @Repository // 구현체에서 만드네.. @Repository
public class MemoryMemberRepository implements MemberRepository {
    // 저장을 할껀데 지금 데이터 베이스가 안정해진 상황
    // Map<Long, Member> store = new HashMap<>(); 메모리에 저장
    // Map<회원 id, 값>
    private static Map<Long, Member> store = new HashMap<>(); //실무에서는 공유되는 변수의 경우 동시성문제때문에 사용하지 않음 (필기해놨음)
    private static long sequence = 0L; //sequence 유니크 키값 생성 얘도 실무에서는 long사용안함

    @Override
    public Member save(Member member) {
        member.setId(++sequence); // domain의 member.java에 위치한 함수인 setId에 시퀀스값을 넣음
        store.put(member.getId(), member); //넣은 id를 get을 이용해 꺼네오고 이것을 map에 저장을 함 MAP(ID, (ID,NAME) ) 형태일듯?
        return member;
    }
    @Override
    public Optional<Member> findById(Long id) {
        // return stroe.get(id) 해도 됨 근데 결과가 없다면...?
        return Optional.ofNullable(store.get(id)); //null이여도 값을 반환해서 클라이언트에서 뭘할수 있음    null이 반환된 가능성이 있다면 Optional.ofNullable 사용
    }
    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }
    @Override
    public Optional<Member> findByName(String name) {
        return store.values().stream()
                .filter(member -> member.getName().equals(name))
                .findAny();
    }
    public void clearStore() {
        store.clear();
    }
}
