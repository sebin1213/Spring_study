package jpabook.jpashop.service;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional // 전체에 붙여줬지만 readOnly로 해놨으니 저장할때는 그냥 트렌젝션이 필요해서 따로 붙여줌
    public Long saveItem(Item item) {
        itemRepository.save(item);
        return item.getId();
    }
    @Transactional
    public void updateItem(Long id, String name, int price){ //Book parm: 파라미터로 넘어온 준영속 상태의 엔티티
        Item item = itemRepository.findOne(id);
        //setter말고 엔티티에 change라는 메서드를 만들어서 보내는게 추적하기에 훨씬 좋음 병합감지와 병합에서 다시보기
        //item.change(name, price, stockQuantity);
        item.setName(name);
        item.setPrice(price);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}