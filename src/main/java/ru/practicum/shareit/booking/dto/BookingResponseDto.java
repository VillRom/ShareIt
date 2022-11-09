package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

/**
 * A DTO for the {@link ru.practicum.shareit.booking.Booking} entity
 */
@Data
public class BookingResponseDto {

    private long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private ItemDto item;

    private UserDto booker;

    private String status;
}