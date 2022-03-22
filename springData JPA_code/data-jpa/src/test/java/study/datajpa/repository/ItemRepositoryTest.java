package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.datajpa.entity.Item;

import static org.junit.jupiter.api.Assertions.*;

// save() persist merge 메서드 테스트
@SpringBootTest
class ItemRepositoryTest {
    @Autowired ItemRepository itemRepository;

    @Test
    public void save(){
        Item item = new Item("A"); // id 값은 jpa에 persist 하면 그때 들어가는거
        itemRepository.save(item); // 트렌젝션 없어도 잘됨
    }
}