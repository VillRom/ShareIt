package ru.practicum.shareit.item;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithDateBookingDto;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@UtilityClass
public class ItemMapper {

    public static ItemDto itemToItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        return itemDto;
    }

    public static Item itemDtoToItem(long userId, ItemDto itemDto) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setUserId(userId);
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        return item;
    }

    public static List<ItemDto> getItemsDtoFromItems(List<Item> items) {
        return items.stream().map(ItemMapper::itemToItemDto).collect(Collectors.toList());
    }

    public static ItemWithDateBookingDto itemToItemWithDateBookingDto(Item item, List<BookingDto> bookingDtoList,
                                                                      List<CommentDto> comments) {
        Optional<BookingDto> last = bookingDtoList.stream().filter(booking -> booking.getEnd()
                .isBefore(LocalDateTime.now())).findFirst();
        Optional<BookingDto> next = bookingDtoList.stream().filter(booking -> booking.getStart()
                .isAfter(LocalDateTime.now())).findFirst();
        ItemWithDateBookingDto itemWithDateBookingDto = new ItemWithDateBookingDto();
        itemWithDateBookingDto.setLastBooking(last.orElse(null));
        itemWithDateBookingDto.setNextBooking(next.orElse(null));
        itemWithDateBookingDto.setAvailable(item.getAvailable());
        itemWithDateBookingDto.setDescription(item.getDescription());
        itemWithDateBookingDto.setName(item.getName());
        itemWithDateBookingDto.setId(item.getId());
        itemWithDateBookingDto.setComments(comments);
        return itemWithDateBookingDto;
    }
}
