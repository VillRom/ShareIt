package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingResponseDto addBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @RequestBody BookingDto booking) {
        return bookingService.addBooking(userId, booking);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto patchBooking(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long bookingId,
                                           @RequestParam Boolean approved) {
        return bookingService.updateStatusBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBookingById(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable long bookingId) {
        return bookingService.getBookingByAuthorOrOwner(userId, bookingId);
    }

    @GetMapping
    public List<BookingResponseDto> getListBookingByUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                                         @RequestParam String state,
                                                         @RequestParam int from,
                                                         @RequestParam int size) {
        return bookingService.getBookingsSort(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getListBookingByItemOwner(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                                              @RequestParam String state,
                                                              @RequestParam int from,
                                                              @RequestParam int size) {
        return bookingService.getBookingsByItemOwner(ownerId, state, from, size);
    }
}
