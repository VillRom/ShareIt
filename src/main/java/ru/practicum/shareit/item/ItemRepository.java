package ru.practicum.shareit.item;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;


public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByUserId(long userId);

    @Query("select i from Item i " +
            "where i.available = true and upper(i.description) like upper(concat('%', ?1, '%')) or " +
            "upper(i.name) like upper(concat('%', ?2, '%'))")
    List<Item> findInAvailableItems(String description, String name);
}
