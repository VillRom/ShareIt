package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.BookingException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemResponseDto;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;

    private final UserRepository userRepository;

    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public ItemRequestDto addRequest(long userId, ItemRequestDto itemRequestDto) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        itemRequestDto.setCreated(LocalDateTime.now());
        return ItemRequestMapper.itemRequestToItemRequestDto(itemRequestRepository.save(ItemRequestMapper
                .itemRequestDtoToItemRequest(userId, itemRequestDto)));
    }

    @Override
    public List<ItemResponseDto> getRequestsByUser(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        List<ItemRequest> itemRequests = itemRequestRepository.findAllByUserId(userId);
        List<ItemResponseDto> itemResponsesDto = new ArrayList<>();
        for (ItemRequest request : itemRequests) {
            itemResponsesDto.add(ItemRequestMapper.itemRequestToItemResponseDto(request,
                    itemRepository.findAllByRequestId(request.getId())));
        }
        return itemResponsesDto;
    }

    @Override
    public ItemResponseDto getRequestByRequestIdByUser(long userId, long requestId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        if (!itemRequestRepository.existsById(requestId)) {
            throw new NotFoundException("Запрос не найден");
        }
        return ItemRequestMapper.itemRequestToItemResponseDto(itemRequestRepository.getReferenceById(requestId),
                itemRepository.findAllByRequestId(requestId));
    }

    @Override
    public List<ItemResponseDto> getAllRequests(long userId, int from, int size) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        if (from < 0 || size <= 0) {
            throw new BookingException("Переданное значение меньше или равно нулю");
        }
        Page<ItemRequest> itemRequests = itemRequestRepository.findAllByUserIdNot(userId, PageRequest.of(from, size,
                Sort.by("created").descending()));
        List<ItemResponseDto> itemResponsesDto = new ArrayList<>();
        for (ItemRequest request : itemRequests) {
            itemResponsesDto.add(ItemRequestMapper.itemRequestToItemResponseDto(request,
                    itemRepository.findAllByRequestId(request.getId())));
        }
        return itemResponsesDto;
    }
}
