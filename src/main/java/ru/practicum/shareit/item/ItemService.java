package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto addItem(long userId, ItemDto itemDto);

    ItemDto findItemById(long userId, long id);

    List<ItemDto> getAllItemsFromUser(long userId);

    ItemDto updateItemById(long userId, ItemDto item, long itemId);

    void deleteItemById(long id);

    List<ItemDto> searchItems(long userId, String query);
}
