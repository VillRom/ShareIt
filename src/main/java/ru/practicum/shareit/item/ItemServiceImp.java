package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.BookingException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithDateBooking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImp implements ItemService {

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    private final BookingRepository bookingRepository;

    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public ItemDto addItem(long userId, ItemDto itemDto) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        return ItemMapper.itemToItemDto(itemRepository.save(ItemMapper.itemDtoToItem(userId, itemDto)));
    }

    @Override
    public ItemWithDateBooking findItemById(long userId, long id) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        if (!itemRepository.existsById(id)) {
            throw new NotFoundException("Такого Item не существует");
        }
        return ItemMapper.itemToItemWithDateBookingDto(itemRepository.getReferenceById(id),
                bookingRepository.findAllByItem_IdAndItem_UserId(id, userId),
                commentRepository.findAllByItem_Id(id));
    }

    @Override
    public List<ItemWithDateBooking> getAllItemsFromUser(long userId, int from, int size) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        if (from < 0 || size <= 0) {
            throw new BookingException("Переданное значение меньше или равно нулю");
        }
        Page<Item> items = itemRepository.findAllByUserId(userId, PageRequest.of(from, size));
        List<ItemWithDateBooking> itemsWithDateBookingDto = new ArrayList<>();
        for (Item item : items) {
            itemsWithDateBookingDto.add(ItemMapper.itemToItemWithDateBookingDto(item,
                    bookingRepository.findAllByItem_IdAndItem_UserId(item.getId(), item.getUserId()),
                    commentRepository.findAllByItem_Id(item.getId())));
        }
        return itemsWithDateBookingDto;
    }

    @Override
    @Transactional
    public ItemDto updateItemById(long userId, ItemDto item, long itemId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        Item updateItem = itemRepository.getReferenceById(itemId);
        if (updateItem.getUserId() != userId) {
            throw new NotFoundException("Неправильно указан пользователь");
        }
        if (item.getName() != null && !updateItem.getName().equals(item.getName())) {
            updateItem.setName(item.getName());
        }
        if (!updateItem.getDescription().equals(item.getDescription()) && item.getDescription() != null) {
            updateItem.setDescription(item.getDescription());
        }
        if (updateItem.getAvailable() != item.getAvailable() && item.getAvailable() != null) {
            updateItem.setAvailable(item.getAvailable());
        }
        return ItemMapper.itemToItemDto(itemRepository.save(updateItem));
    }

    @Override
    public List<ItemDto> searchItems(long userId, String query) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        if (query.isBlank()) {
            return new ArrayList<>(List.of());
        }
        return ItemMapper.getItemsDtoFromItems(itemRepository.findInAvailableItems(query, query));
    }

    @Override
    @Transactional
    public CommentDto addComment(long userId, long itemId, CommentDto comment) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        if (!itemRepository.existsById(itemId)) {
            throw new NotFoundException("Вещь не найдена");
        }
        if (bookingRepository.findAllByItem_IdAndBooker_IdAndEndIsBefore(itemId, userId, LocalDateTime.now())
                .isEmpty()) {
            throw new BookingException("Вы не можете оставить отзыв на эту вещь");
        }
        comment.setCreated(LocalDateTime.now());
        return CommentMapper.commentToCommentDto(commentRepository.save(CommentMapper
                .commentDtoToComment(itemRepository.getReferenceById(itemId),
                        userRepository.getReferenceById(userId), comment)));
    }
}
