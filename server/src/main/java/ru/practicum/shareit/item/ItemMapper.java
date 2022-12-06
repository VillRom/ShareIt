package ru.practicum.shareit.item;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithDateBooking;
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
        itemDto.setRequestId(item.getRequestId());
        return itemDto;
    }

    public static Item itemDtoToItem(long userId, ItemDto itemDto) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setUserId(userId);
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setRequestId(itemDto.getRequestId());
        return item;
    }

    public static List<ItemDto> getItemsDtoFromItems(List<Item> items) {
        return items.stream().map(ItemMapper::itemToItemDto).collect(Collectors.toList());
    }

    public static ItemWithDateBooking itemToItemWithDateBookingDto(Item item, List<Booking> bookingList,
                                                                   List<Comment> comments) {
        Optional<Booking> last = bookingList.stream().filter(booking -> booking.getEnd()
                .isBefore(LocalDateTime.now())).findFirst();
        Optional<Booking> next = bookingList.stream().filter(booking -> booking.getStart()
                .isAfter(LocalDateTime.now())).findFirst();
        ItemWithDateBooking itemWithDateBookingDto = new ItemWithDateBooking();
        if (last.isEmpty()) {
            itemWithDateBookingDto.setLastBooking(null);
        } else {
            itemWithDateBookingDto.setLastBooking(new ItemWithDateBooking.Booking(last.get().getId(),
                    last.get().getBooker().getId()));
        }
        if (next.isEmpty()) {
            itemWithDateBookingDto.setNextBooking(null);
        } else {
            itemWithDateBookingDto.setNextBooking(new ItemWithDateBooking.Booking(next.get().getId(),
                    next.get().getBooker().getId()));
        }
        itemWithDateBookingDto.setAvailable(item.getAvailable());
        itemWithDateBookingDto.setDescription(item.getDescription());
        itemWithDateBookingDto.setName(item.getName());
        itemWithDateBookingDto.setId(item.getId());
        itemWithDateBookingDto.setComments(commentsToItemWithDateBookingComments(comments));
        return itemWithDateBookingDto;
    }

    private ItemWithDateBooking.Comment commentToItemWithDateBookingComment(Comment comment) {
        return new ItemWithDateBooking.Comment(comment.getId(), comment.getText(),
                comment.getAuthor().getName(), comment.getCreated());
    }

    private List<ItemWithDateBooking.Comment> commentsToItemWithDateBookingComments(List<Comment> comments) {
        return comments.stream().map(ItemMapper::commentToItemWithDateBookingComment).collect(Collectors.toList());
    }
}
