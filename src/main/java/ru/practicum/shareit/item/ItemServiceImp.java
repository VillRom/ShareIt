package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImp implements ItemService {

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    @Override
    public ItemDto addItem(long userId, ItemDto itemDto) {
        if (userRepository.getUserById(userId) == null) {
            throw new NotFoundException("Такого пользователя не существует");
        }
        return ItemMapper.itemToItemDto(itemRepository.saveItem(ItemMapper.itemDtoToItem(userId, itemDto)));
    }

    @Override
    public ItemDto findItemById(long userId, long id) {
        if (userRepository.getUserById(userId) == null) {
            throw new NotFoundException("Такого пользователя не существует");
        }
        return ItemMapper.itemToItemDto(itemRepository.getItemById(id));
    }

    @Override
    public List<ItemDto> getAllItemsFromUser(long userId) {
        if (userRepository.getUserById(userId) == null) {
            throw new NotFoundException("Такого пользователя не существует");
        }
        return ItemMapper.getItemsDtoFromItems(itemRepository.getAllItems(userId));
    }

    @Override
    public ItemDto updateItemById(long userId, ItemDto item, long itemId) {
        if (userRepository.getUserById(userId) == null) {
            throw new NotFoundException("Такого пользователя не существует");
        }
        if (itemRepository.getItemById(itemId).getUserId() != userId) {
            throw new NotFoundException("Неправильно указан пользователь");
        }
        return ItemMapper.itemToItemDto(itemRepository.updateItem(itemId, ItemMapper.itemDtoToItem(userId, item)));
    }

    @Override
    public void deleteItemById(long id) {
        itemRepository.deleteItemById(id);
    }

    @Override
    public List<ItemDto> searchItems(long userId, String query) {
        if (userRepository.getUserById(userId) == null) {
            throw new NotFoundException("Такого пользователя не существует");
        }
        return ItemMapper.getItemsDtoFromItems(itemRepository.search(query));
    }
}
