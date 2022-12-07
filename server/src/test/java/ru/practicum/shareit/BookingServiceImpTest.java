package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.BookingServiceImp;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.exception.BookingException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BookingServiceImpTest {

    private BookingService bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    private final BookingDto bookingDto = new BookingDto();

    private final Booking booking = new Booking();

    private final Item item = new Item();

    private final User user = new User();


    @BeforeEach
    void setUp() {
        bookingService = new BookingServiceImp(bookingRepository, itemRepository, userRepository);
        bookingDto.setId(1);
        bookingDto.setEnd(LocalDateTime.now().plusSeconds(60));
        bookingDto.setStart(LocalDateTime.now().plusSeconds(5));
        bookingDto.setBookerId(1);
        bookingDto.setItemId(1L);
        item.setId(1L);
        item.setUserId(1);
        item.setName("name");
        item.setRequestId(1L);
        item.setAvailable(true);
        item.setDescription("description");
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
    void addBookingTest() {
        when(itemRepository.getReferenceById(anyLong()))
                .thenReturn(item);
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(itemRepository.existsById(anyLong()))
                .thenReturn(true);
        when(bookingRepository.save(any()))
                .thenReturn(booking);
        when(userRepository.getReferenceById(anyLong()))
                .thenReturn(user);
        BookingResponseDto bookingResponseDtoTest = bookingService.addBooking(2, bookingDto);
        Assertions.assertEquals(bookingResponseDtoTest.getId(), 1);
        Assertions.assertEquals(bookingResponseDtoTest.getBooker().getName(), "name");
    }

    @Test
    void addBookingTestBookingExceptionTest() {
        item.setAvailable(false);
        when(itemRepository.getReferenceById(anyLong()))
                .thenReturn(item);
        final BookingException exception = Assertions.assertThrows(
                BookingException.class,
                () -> bookingService.addBooking(1, bookingDto));
        Assertions.assertEquals("Бронорование не доступно", exception.getMessage());
    }

    @Test
    void addBookingTestNotFoundExceptionUserTest() {
        when(itemRepository.getReferenceById(anyLong()))
                .thenReturn(item);
        when(userRepository.existsById(anyLong()))
                .thenReturn(false);
        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> bookingService.addBooking(1, bookingDto));
        Assertions.assertEquals("Пользователь не найден", exception.getMessage());
    }

    @Test
    void addBookingTestNotFoundExceptionItemTest() {
        when(itemRepository.getReferenceById(anyLong()))
                .thenReturn(item);
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(itemRepository.existsById(anyLong()))
                .thenReturn(false);
        final EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> bookingService.addBooking(1, bookingDto));
        Assertions.assertEquals("Вещь не найдена", exception.getMessage());
    }

    @Test
    void getBookingByAuthorOrOwnerTest() {
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(bookingRepository.getReferenceById(anyLong()))
                .thenReturn(booking);
        BookingResponseDto bookingResponseDtoTest = bookingService.getBookingByAuthorOrOwner(1, 1);
        Assertions.assertEquals(bookingResponseDtoTest.getId(), 1);
        Assertions.assertEquals(bookingResponseDtoTest.getStatus(), "WAITING");
    }

    @Test
    void getBookingByAuthorOrOwnerNotFoundExceptionUserTest() {
        when(userRepository.existsById(anyLong()))
                .thenReturn(false);
        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> bookingService.getBookingByAuthorOrOwner(1, 1));
        Assertions.assertEquals("Пользователь не найден", exception.getMessage());
    }

    @Test
    void getBookingByAuthorOrOwnerNotFoundExceptionTest() {
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(bookingRepository.getReferenceById(anyLong()))
                .thenReturn(booking);
        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> bookingService.getBookingByAuthorOrOwner(2, 1));
        Assertions.assertEquals("В доступе отказано", exception.getMessage());
    }

    @Test
    void getBookingsSortAllTest() {
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(bookingRepository.findAllByBooker_IdOrderByStartDesc(anyLong(), any()))
                .thenReturn(Page.empty());
        List<BookingResponseDto> bookingResponsesDto = bookingService
                .getBookingsSort(1, "ALL", 1, 10);
        Assertions.assertEquals(bookingResponsesDto.size(), 0);
    }

    @Test
    void getBookingsSortCURRENTTest() {
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(bookingRepository.findAllByBooker_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(anyLong(), any(),
                any(), any())).thenReturn(Page.empty());
        List<BookingResponseDto> bookingResponsesDto = bookingService
                .getBookingsSort(1, "CURRENT", 1, 10);
        Assertions.assertEquals(bookingResponsesDto.size(), 0);
    }

    @Test
    void getBookingsSortFUTURETest() {
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(bookingRepository.findAllByBooker_IdAndStartIsAfterOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(Page.empty());
        List<BookingResponseDto> bookingResponsesDto = bookingService
                .getBookingsSort(1, "FUTURE", 1, 10);
        Assertions.assertEquals(bookingResponsesDto.size(), 0);
    }

    @Test
    void getBookingsSortPASTest() {
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(bookingRepository.findAllByBooker_IdAndEndIsBeforeOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(Page.empty());
        List<BookingResponseDto> bookingResponsesDto = bookingService
                .getBookingsSort(1, "PAST", 1, 10);
        Assertions.assertEquals(bookingResponsesDto.size(), 0);
    }

    @Test
    void getBookingsSortWAITINGTest() {
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(bookingRepository.findAllByBooker_IdAndStatusContainingOrderByStartDesc(anyLong(),anyString(), any()))
                .thenReturn(Page.empty());
        List<BookingResponseDto> bookingResponsesDto = bookingService
                .getBookingsSort(1, "WAITING", 1, 10);
        Assertions.assertEquals(bookingResponsesDto.size(), 0);
    }

    @Test
    void getBookingsSortREJECTEDTest() {
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(bookingRepository.findAllByBooker_IdAndStatusContainingOrderByStartDesc(anyLong(),anyString(), any()))
                .thenReturn(Page.empty());
        List<BookingResponseDto> bookingResponsesDto = bookingService
                .getBookingsSort(1, "REJECTED", 1, 10);
        Assertions.assertEquals(bookingResponsesDto.size(), 0);
    }

    @Test
    void getBookingsSortBookingExceptionTest() {
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        final BookingException exception = Assertions.assertThrows(
                BookingException.class,
                () -> bookingService.getBookingsSort(1, "UNKNOWN", 1, 10));
        Assertions.assertEquals("Unknown state: UNKNOWN", exception.getMessage());
    }

    @Test
    void getBookingsSortNotFoundExceptionUserTest() {
        when(userRepository.existsById(anyLong()))
                .thenReturn(false);
        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> bookingService.getBookingsSort(1, "ALL", 1, 10));
        Assertions.assertEquals("Пользователь не найден", exception.getMessage());
    }

    @Test
    void getBookingsSortBookingZeroExceptionTest() {
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        final BookingException exception = Assertions.assertThrows(
                BookingException.class,
                () -> bookingService.getBookingsSort(1, "ALL", 0, 10));
        Assertions.assertEquals("Переданное значение меньше или равно нулю", exception.getMessage());
    }

    @Test
    void getBookingsByItemOwnerALLTest() {
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(bookingRepository.findAllByItem_UserIdOrderByStartDesc(anyLong(), any()))
                .thenReturn(Page.empty());
        List<BookingResponseDto> bookingResponsesDto = bookingService
                .getBookingsByItemOwner(1, "ALL", 1, 10);
        Assertions.assertEquals(bookingResponsesDto.size(), 0);
    }

    @Test
    void getBookingsByItemOwnerCURRENTTest() {
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(bookingRepository.findAllByItem_UserIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(anyLong(), any(),
                any(), any())).thenReturn(Page.empty());
        List<BookingResponseDto> bookingResponsesDto = bookingService
                .getBookingsByItemOwner(1, "CURRENT", 1, 10);
        Assertions.assertEquals(bookingResponsesDto.size(), 0);
    }

    @Test
    void getBookingsByItemOwnerFUTURETest() {
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(bookingRepository.findAllByItem_UserIdAndStartIsAfterOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(Page.empty());
        List<BookingResponseDto> bookingResponsesDto = bookingService
                .getBookingsByItemOwner(1, "FUTURE", 1, 10);
        Assertions.assertEquals(bookingResponsesDto.size(), 0);
    }

    @Test
    void getBookingsByItemOwnerPASTTest() {
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(bookingRepository.findAllByItem_UserIdAndEndIsBeforeOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(Page.empty());
        List<BookingResponseDto> bookingResponsesDto = bookingService
                .getBookingsByItemOwner(1, "PAST", 1, 10);
        Assertions.assertEquals(bookingResponsesDto.size(), 0);
    }

    @Test
    void getBookingsByItemOwnerWAITINGTest() {
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(bookingRepository.findAllByItem_UserIdAndStatusContainingOrderByStartDesc(anyLong(), anyString(), any()))
                .thenReturn(Page.empty());
        List<BookingResponseDto> bookingResponsesDto = bookingService
                .getBookingsByItemOwner(1, "WAITING", 1, 10);
        Assertions.assertEquals(bookingResponsesDto.size(), 0);
    }

    @Test
    void getBookingsByItemOwnerREJECTEDTest() {
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(bookingRepository.findAllByItem_UserIdAndStatusContainingOrderByStartDesc(anyLong(),anyString(), any()))
                .thenReturn(Page.empty());
        List<BookingResponseDto> bookingResponsesDto = bookingService
                .getBookingsByItemOwner(1, "REJECTED", 1, 10);
        Assertions.assertEquals(bookingResponsesDto.size(), 0);
    }

    @Test
    void getBookingsByItemOwnerNotFoundExceptionTest() {
        when(userRepository.existsById(anyLong()))
                .thenReturn(false);
        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> bookingService.getBookingsByItemOwner(1, "ALL", 1, 10));
        Assertions.assertEquals("Пользователь не найден", exception.getMessage());
    }

    @Test
    void getBookingsByItemOwnerBookingZeroExceptionTest() {
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        final BookingException exception = Assertions.assertThrows(
                BookingException.class,
                () -> bookingService.getBookingsByItemOwner(1, "ALL", -1, 10));
        Assertions.assertEquals("Переданное значение меньше или равно нулю", exception.getMessage());
    }

    @Test
    void getBookingsByItemOwnerBookingExceptionTest() {
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        final BookingException exception = Assertions.assertThrows(
                BookingException.class,
                () -> bookingService.getBookingsByItemOwner(1, "UNKNOWN", 0, 10));
        Assertions.assertEquals("Unknown state: UNKNOWN", exception.getMessage());
    }

    @Test
    void updateStatusBookingAPPROVEDTest() {
        when(bookingRepository.getReferenceById(anyLong()))
                .thenReturn(booking);
        Booking bookingTest = new Booking();
        bookingTest.setId(1L);
        bookingTest.setStatus("APPROVED");
        bookingTest.setStart(LocalDateTime.now().plusSeconds(5));
        bookingTest.setEnd(LocalDateTime.now().plusSeconds(60));
        bookingTest.setItem(item);
        bookingTest.setBooker(user);
        when(bookingRepository.save(any()))
                .thenReturn(bookingTest);
        BookingResponseDto bookingResponseDtoTest = bookingService.updateStatusBooking(1, 1, true);
        Assertions.assertEquals(bookingResponseDtoTest.getStatus(), "APPROVED");
    }

    @Test
    void updateStatusBookingREJECTEDTest() {
        when(bookingRepository.getReferenceById(anyLong()))
                .thenReturn(booking);
        Booking bookingTest = new Booking();
        bookingTest.setId(1L);
        bookingTest.setStatus("REJECTED");
        bookingTest.setStart(LocalDateTime.now().plusSeconds(5));
        bookingTest.setEnd(LocalDateTime.now().plusSeconds(60));
        bookingTest.setItem(item);
        bookingTest.setBooker(user);
        when(bookingRepository.save(any()))
                .thenReturn(bookingTest);
        BookingResponseDto bookingResponseDtoTest = bookingService.updateStatusBooking(1, 1, false);
        Assertions.assertEquals(bookingResponseDtoTest.getStatus(), "REJECTED");
    }

    @Test
    void updateStatusBookingBookingExceptionTest() {
        booking.setStatus("REJECTED");
        when(bookingRepository.getReferenceById(anyLong()))
                .thenReturn(booking);
        final BookingException exception = Assertions.assertThrows(
                BookingException.class,
                () -> bookingService.updateStatusBooking(1, 1, true));
        Assertions.assertEquals("Статус запроса уже изменен", exception.getMessage());
    }

    @Test
    void updateStatusBookingNotFoundExceptionTest() {
        when(bookingRepository.getReferenceById(anyLong()))
                .thenReturn(booking);
        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> bookingService.updateStatusBooking(5, 1, true));
        Assertions.assertEquals("Вещь не найдена", exception.getMessage());
    }
}
