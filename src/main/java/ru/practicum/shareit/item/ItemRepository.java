package ru.practicum.shareit.item;


import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {

    Item saveItem(Item item);

    Item getItemById(long itemId);

    Item updateItem(long itemId, Item item);

    void deleteItemById(long itemId);

    List<Item> getAllItems(long itemId);

    List<Item> search(String query);
}
