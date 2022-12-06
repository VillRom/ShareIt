package ru.practicum.shareit.request;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemResponseDto;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ItemRequestMapper {

    public static ItemRequestDto itemRequestToItemRequestDto(ItemRequest itemRequest) {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setId(itemRequest.getId());
        requestDto.setDescription(itemRequest.getDescription());
        requestDto.setCreated(itemRequest.getCreated());
        return requestDto;
    }

    public static ItemRequest itemRequestDtoToItemRequest(long userId, ItemRequestDto requestDto) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setCreated(requestDto.getCreated());
        itemRequest.setDescription(requestDto.getDescription());
        itemRequest.setId(requestDto.getId());
        itemRequest.setUserId(userId);
        return itemRequest;
    }

    public static List<ItemRequestDto> itemRequestsToItemRequestsDto(List<ItemRequest> requests) {
       return requests.stream().map(ItemRequestMapper::itemRequestToItemRequestDto).collect(Collectors.toList());
    }

    public static ItemResponseDto itemRequestToItemResponseDto(ItemRequest itemRequest, List<Item> items) {
        ItemResponseDto itemResponseDto = new ItemResponseDto();
        itemResponseDto.setId(itemRequest.getId());
        itemResponseDto.setDescription(itemRequest.getDescription());
        itemResponseDto.setCreated(itemRequest.getCreated());
        itemResponseDto.setItems(itemsToItemResponseDtoItems(items));
        return itemResponseDto;
    }

    private ItemResponseDto.Item itemToItemResponseDtoItem(Item item) {
        return new ItemResponseDto.Item(item.getId(), item.getName(), item.getDescription(), item.getAvailable(),
                item.getRequestId());
    }

    private List<ItemResponseDto.Item> itemsToItemResponseDtoItems(List<Item> items) {
        return items.stream().map(ItemRequestMapper::itemToItemResponseDtoItem).collect(Collectors.toList());
    }
}
