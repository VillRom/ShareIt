package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemResponseDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto addRequest(long userId, ItemRequestDto itemRequestDto);

    List<ItemResponseDto> getRequestsByUser(long userId);

    ItemResponseDto getRequestByRequestIdByUser(long userId, long requestId);

    List<ItemResponseDto> getAllRequests(long userId, int from, int size);
}
