package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryImp implements ItemRepository {

    private long id = 1;

    private final Map<Long, Item> storageItem = new HashMap<>();

    @Override
    public Item saveItem(Item item) {
        item.setId(id);
        id++;
        storageItem.put(item.getId(), item);
        return item;
    }

    @Override
    public Item getItemById(long itemId) {
        return storageItem.get(itemId);
    }

    @Override
    public Item updateItem(long itemId, Item item) {
        Item upItem = getItemById(itemId);
        if (item.getName() != null && !upItem.getName().equals(item.getName())) {
            upItem.setName(item.getName());
        }
        if (!upItem.getDescription().equals(item.getDescription()) && item.getDescription() != null) {
            upItem.setDescription(item.getDescription());
        }
        if (upItem.getAvailable() != item.getAvailable() && item.getAvailable() != null) {
            upItem.setAvailable(item.getAvailable());
        }
        return upItem;
    }

    @Override
    public void deleteItemById(long itemId) {
        storageItem.remove(itemId);
    }

    @Override
    public List<Item> getAllItems(long itemId) {
        return storageItem.values().stream().filter(item -> item.getUserId() == itemId).collect(Collectors.toList());
    }

    @Override
    public List<Item> search(String query) {
        String search = query.toLowerCase();
        List<Item> sortedListByUserAndAvailable = storageItem.values().stream()
                .filter(Item::getAvailable).collect(Collectors.toList());
        List<Item> searchResult = new ArrayList<>();
        for (Item item : sortedListByUserAndAvailable) {
            if (!search.isBlank() && (item.getName().toLowerCase().contains(search)
                    || item.getDescription().toLowerCase().contains(search))) {
                searchResult.add(item);
            }
        }
        return searchResult;
    }
}
