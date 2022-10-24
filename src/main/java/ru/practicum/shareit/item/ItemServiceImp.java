package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.validation.Validation;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImp implements ItemService {

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    private final MapperItem mapperItem;


    private final Validation validation;

    @Override
    public ItemDto addItem(long userId, ItemDto itemDto) {
        if (userRepository.getUserById(userId) == null) {
            throw new NotFoundException("Такого пользователя не существует");
        }
        validation.validationItemDto(itemDto);
        return mapperItem.itemToItemDto(itemRepository.saveItem(mapperItem.ItemDtoToItem(userId, itemDto)));
    }

    @Override
    public ItemDto findItemById(long userId, long id) {
        if (userRepository.getUserById(userId) == null) {
            throw new NotFoundException("Такого пользователя не существует");
        }
        return mapperItem.itemToItemDto(itemRepository.getItemById(id));
    }

    @Override
    public List<ItemDto> getAllItemsFromUser(long userId) {
        if (userRepository.getUserById(userId) == null) {
            throw new NotFoundException("Такого пользователя не существует");
        }
        return mapperItem.getItemsDtoFromItems(itemRepository.getAllItems(userId));
    }

    @Override
    public ItemDto updateItemById(long userId, ItemDto item, long itemId) {
        if (userRepository.getUserById(userId) == null) {
            throw new NotFoundException("Такого пользователя не существует");
        }
        if (itemRepository.getItemById(itemId).getUserId() != userId) {
            throw new NotFoundException("Неправильно указан пользователь");
        }
        return mapperItem.itemToItemDto(itemRepository.updateItem(itemId, mapperItem.ItemDtoToItem(userId, item)));
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
        return mapperItem.getItemsDtoFromItems(itemRepository.search(query));
    }
}
