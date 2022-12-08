package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.BookingException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.ItemServiceImp;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithDateBooking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ItemServiceImpTest {

    private ItemService itemService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private CommentRepository commentRepository;

    private final ItemDto itemDto = new ItemDto();

    private final CommentDto commentDto = new CommentDto();

    private final Item item = new Item();

    private final Booking booking = new Booking();

    private final User user = new User();

    private final Comment comment = new Comment();

    @BeforeEach
    void setUp() {
        itemService = new ItemServiceImp(itemRepository, userRepository, bookingRepository, commentRepository);
        itemDto.setId(1);
        itemDto.setDescription("description");
        itemDto.setName("name");
        itemDto.setAvailable(true);
        itemDto.setRequestId(1L);
        commentDto.setId(1);
        commentDto.setAuthorName("author name");
        commentDto.setText("text");
        commentDto.setCreated(LocalDateTime.now());
        user.setId(1);
        user.setName("name");
        user.setEmail("name@mail.ru");
        booking.setId(1L);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusSeconds(15));
        booking.setStatus("Waiting");
        booking.setItem(item);
        booking.setBooker(user);
        comment.setAuthor(user);
        comment.setItem(item);
        comment.setText("text");
        comment.setId(1);
        comment.setCreated(LocalDateTime.now());
        item.setName("name");
        item.setUserId(1);
        item.setId(1L);
        item.setDescription("description");
        item.setRequestId(1L);
        item.setAvailable(true);
    }

    @Test
    void addItemTest() {
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(itemRepository.save(any()))
                .thenReturn(item);
        ItemDto itemDtoTest = itemService.addItem(1, itemDto);
        Assertions.assertEquals(itemDtoTest.getName(), "name");
        Assertions.assertEquals(itemDtoTest.getId(), 1);
    }

    @Test
    void addItemExceptionTest() {
        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> itemService.addItem(1, itemDto));
        Assertions.assertEquals("Пользователь не найден", exception.getMessage());
    }

    @Test
    void findItemByIdTest() {
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(itemRepository.existsById(anyLong()))
                .thenReturn(true);
        when(itemRepository.getReferenceById(anyLong()))
                .thenReturn(item);
        when(bookingRepository.findAllByItem_IdAndItem_UserId(anyLong(), anyLong()))
                .thenReturn(List.of(booking));
        when(commentRepository.findAllByItem_Id(anyLong()))
                .thenReturn(List.of(comment));
        ItemWithDateBooking itemWithDateBookingTest = itemService.findItemById(1, 1);
        Assertions.assertEquals(itemWithDateBookingTest.getId(), 1);
        Assertions.assertEquals(itemWithDateBookingTest.getName(), "name");
    }

    @Test
    void findItemByIdExceptionUserTest() {
        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> itemService.findItemById(1, 1));
        Assertions.assertEquals("Пользователь не найден", exception.getMessage());
    }

    @Test
    void findItemByIdExceptionItemTest() {
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> itemService.findItemById(1, 1));
        Assertions.assertEquals("Такого Item не существует", exception.getMessage());
    }

    @Test
    void getAllItemsFromUserTest() {
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(itemRepository.findAllByUserId(anyLong(), any()))
                .thenReturn(Page.empty());
        when(bookingRepository.findAllByItem_IdAndItem_UserId(anyLong(), anyLong()))
                .thenReturn(List.of());
        List<ItemWithDateBooking> itemWithDateBookingTest = itemService.getAllItemsFromUser(1, 0, 10);
        Assertions.assertEquals(itemWithDateBookingTest.size(), 0);
    }

    @Test
    void getAllItemsFromUserExceptionUserTest() {
        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> itemService.getAllItemsFromUser(1, 0, 10));
        Assertions.assertEquals("Пользователь не найден", exception.getMessage());
    }

    @Test
    void getAllItemsFromUserExceptionBookerTest() {
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        final BookingException exception = Assertions.assertThrows(
                BookingException.class,
                () -> itemService.getAllItemsFromUser(1, 0, -1));
        Assertions.assertEquals("Переданное значение меньше или равно нулю", exception.getMessage());
    }

    @Test
    void updateItemByIdTest() {
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(itemRepository.getReferenceById(anyLong()))
                .thenReturn(item);
        when(itemRepository.save(any()))
                .thenReturn(item);
        ItemDto itemDtoTest = itemService.updateItemById(1, itemDto, 1);
        Assertions.assertEquals(itemDtoTest.getName(), "name");
        Assertions.assertEquals(itemDtoTest.getId(), 1);
    }

    @Test
    void updateItemByIdExceptionTest() {
        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> itemService.updateItemById(1, itemDto, 1));
        Assertions.assertEquals("Пользователь не найден", exception.getMessage());
    }

    @Test
    void searchItemsTest() {
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(itemRepository.findInAvailableItems(anyString(), anyString()))
                .thenReturn(List.of(item));
        List<ItemDto> itemsDto = itemService.searchItems(1, "name");
        Assertions.assertEquals(itemsDto.size(), 1);
        Assertions.assertEquals(itemsDto.get(0).getName(), "name");
    }

    @Test
    void searchItemsEmptyTest() {
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        List<ItemDto> itemsDto = itemService.searchItems(1, "");
        Assertions.assertEquals(itemsDto.size(), 0);
    }

    @Test
    void searchItemsExceptionTest() {
        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> itemService.searchItems(1, "name"));
        Assertions.assertEquals("Пользователь не найден", exception.getMessage());
    }

    @Test
    void addCommentTest() {
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(itemRepository.existsById(anyLong()))
                .thenReturn(true);
        when(bookingRepository.findAllByItem_IdAndBooker_IdAndEndIsBefore(anyLong(), anyLong(), any()))
                .thenReturn(List.of(booking));
        when(commentRepository.save(any()))
                .thenReturn(comment);
        when(itemRepository.getReferenceById(anyLong()))
                .thenReturn(item);
        when(userRepository.getReferenceById(anyLong()))
                .thenReturn(user);
        CommentDto commentDtoTest = itemService.addComment(1, 1, commentDto);
        Assertions.assertEquals(commentDtoTest.getId(), 1);
    }

    @Test
    void addCommentExceptionUserTest() {
        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> itemService.addComment(1, 1, commentDto));
        Assertions.assertEquals("Пользователь не найден", exception.getMessage());
    }

    @Test
    void addCommentExceptionItemTest() {
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> itemService.addComment(1, 1, commentDto));
        Assertions.assertEquals("Вещь не найдена", exception.getMessage());
    }

    @Test
    void addCommentExceptionBookingTest() {
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(itemRepository.existsById(anyLong()))
                .thenReturn(true);
        final BookingException exception = Assertions.assertThrows(
                BookingException.class,
                () -> itemService.addComment(1, 1, commentDto));
        Assertions.assertEquals("Вы не можете оставить отзыв на эту вещь", exception.getMessage());
    }
}
