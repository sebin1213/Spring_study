package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.item.Album;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // 트랜젝션 커밋을 안하고 그냥 롤백함
class ItemServiceTest {

    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @Test
    void 회원가입() throws Exception{
        //given

        Book book = new Book();
        book.setName("hope");
        book.setPrice(100);

        //when
        Long saveId = itemService.saveItem(book);  // 리턴타입 없음

        //then
        assertEquals(book, itemService.findOne(saveId));

    }
}
