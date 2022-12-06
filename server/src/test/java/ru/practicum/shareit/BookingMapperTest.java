package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class BookingMapperTest {

    private final Booking booking = new Booking();

    @BeforeEach
    void setUp() {
        Item item = new Item();
            item.setId(1L);
            item.setUserId(1);
            item.setName("name");
            item.setRequestId(1L);
            item.setAvailable(true);
            item.setDescription("description");
        User user = new User();
            user.setId(1L);
            user.setName("name");
            user.setEmail("name@mail.ru");
        booking.setId(1L);
        booking.setStatus("WAITING");
        booking.setStart(LocalDateTime.now().plusSeconds(5));
        booking.setEnd(LocalDateTime.now().plusSeconds(60));
        booking.setItem(item);
        booking.setBooker(user);
    }

    @Test
    void bookingToBookingDtoTest() {
        BookingDto bookingDtoTest = BookingMapper.bookingToBookingDto(booking);
        Assertions.assertEquals(bookingDtoTest.getItemId(), 1);
        Assertions.assertEquals(bookingDtoTest.getId(), 1);
        Assertions.assertEquals(bookingDtoTest.getBookerId(), 1);
    }

    @Test
    void bookingsToBookingDtoListTest() {
        List<BookingDto> bookingsDto = BookingMapper.bookingsToBookingDtoList(List.of(booking));
        Assertions.assertEquals(bookingsDto.size(), 1);
        Assertions.assertEquals(bookingsDto.get(0).getId(), 1);
    }
}
