package ru.practicum.shareit.item;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;


public interface ItemRepository extends JpaRepository<Item, Long> {

    Page<Item> findAllByUserId(long userId, Pageable pageable);

    @Query("select i from Item i " +
            "where i.available = true and upper(i.description) like upper(concat('%', ?1, '%')) or " +
            "upper(i.name) like upper(concat('%', ?2, '%'))")
    List<Item> findInAvailableItems(String description, String name);

    List<Item> findAllByRequestId(long requestId);
}
