package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

public interface BookingService {

    BookingResponseDto addBooking(long userId, BookingDto booking);

    BookingResponseDto getBookingByAuthorOrOwner(long authorId, long bookingId);

    List<BookingResponseDto> getBookingsSort(long authorId, String state, int from, int size);

    BookingResponseDto updateStatusBooking(long authorId, long bookingId, boolean approved);

    List<BookingResponseDto> getBookingsByItemOwner(long ownerId, String state, int from, int size);
}
