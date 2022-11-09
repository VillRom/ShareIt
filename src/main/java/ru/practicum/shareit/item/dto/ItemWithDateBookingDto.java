package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

/**
 * A DTO for the {@link ru.practicum.shareit.item.model.Item} entity
 */
@Data
public class ItemWithDateBookingDto {

    private long id;

    private String name;

    private String description;

    private Boolean available;

    private BookingDto lastBooking;

    private BookingDto nextBooking;

    private List<CommentDto> comments;
}