package ru.practicum.shareit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
public class ItemRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ItemRepository itemRepository;

    private final Item item = new Item();

    private final Item itemTwo = new Item();

    private final User user = new User();

    private final ItemRequest itemRequest = new ItemRequest();

    @BeforeEach
    void setUp() {
        item.setAvailable(true);
        item.setName("name");
        item.setDescription("description");
        item.setUserId(1);
        user.setName("nameUser");
        user.setEmail("email@mail.ru");
        itemTwo.setAvailable(true);
        itemTwo.setName("nameTwo");
        itemTwo.setDescription("descriptionTwo");
        itemTwo.setUserId(1);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setDescription("descr");
        itemRequest.setUserId(1);
    }

    @Test
    void findInAvailableItemsTest() {
        testEntityManager.persist(user);
        testEntityManager.persist(item);
        testEntityManager.persist(itemTwo);
        List<Item> itemsTest = itemRepository.findInAvailableItems("descriptionTwo", "name");
        Assertions.assertThat(itemsTest.size()).isEqualTo(2);
        Assertions.assertThat(itemsTest.get(0).getId()).isEqualTo(1L);
        Assertions.assertThat(itemsTest.get(0).getName()).isEqualTo("name");
        Assertions.assertThat(itemsTest.get(1).getName()).isEqualTo("nameTwo");
    }

    @Test
    void findAllByUserIdTest() {
        item.setUserId(2);
        itemTwo.setUserId(2);
        testEntityManager.persist(user);
        testEntityManager.persist(item);
        testEntityManager.persist(itemTwo);
        Page<Item> items = itemRepository.findAllByUserId(2, PageRequest.of(0, 10));
        Assertions.assertThat(items.getTotalElements()).isEqualTo(2);
        Assertions.assertThat(items.getContent().get(1).getId()).isEqualTo(4);
    }
}
