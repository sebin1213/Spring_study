package jpabook.jpashop.repository;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final EntityManager em;

    public void save(Item item) { // 왜 얘는 이렇게 짰을까..? merge를 위해.. 실무에서는 X
        if (item.getId() == null) { // 아이디 값이 없다는건 신규로 등록한다는 뜻
            em.persist(item);

        } else {
            em.merge(item); // 업데이트와 비슷함
        }
    }
    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }
    public List<Item> findAll() {
        return em.createQuery("select i from Item i",Item.class).getResultList();
    }
}