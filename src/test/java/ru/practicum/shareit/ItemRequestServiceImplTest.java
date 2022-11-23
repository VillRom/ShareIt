package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.exception.BookingException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.ItemRequestServiceImpl;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemResponseDto;
import ru.practicum.shareit.user.UserRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class ItemRequestServiceImplTest {

    @Mock
    private ItemRequestRepository itemRequestRepository;

    private ItemRequestService itemRequestService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    private ItemRequestDto itemRequestDto;

    private final ItemRequest itemRequest = new ItemRequest();

    private final Item item = new Item();

    @BeforeEach
    void setUp() {
        itemRequestService = new ItemRequestServiceImpl(itemRequestRepository, userRepository,
                itemRepository);
        itemRequestDto = new ItemRequestDto(1,"Description", LocalDateTime.now());
        itemRequest.setId(1);
        itemRequest.setDescription("Description");
        itemRequest.setUserId(1);
        itemRequest.setCreated(itemRequestDto.getCreated());
        item.setName("item");
        item.setUserId(1);
        item.setId(1L);
        item.setDescription("description");
        item.setRequestId(1L);
        item.setAvailable(true);
    }

    @Test
    void addRequestTest() {
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(itemRequestRepository.save(any(ItemRequest.class)))
                .thenReturn(itemRequest);
        ItemRequestDto itemRequestDtoTest = itemRequestService.addRequest(1L, itemRequestDto);
        Assertions.assertEquals(itemRequestDtoTest.getId(), itemRequestDto.getId());
        Assertions.assertEquals(itemRequestDtoTest.getDescription(), itemRequestDto.getDescription());
    }
    @Test
    void addRequestExceptionTest() {
        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> itemRequestService.addRequest(1, itemRequestDto));
        Assertions.assertEquals("Пользователь не найден", exception.getMessage());
    }

    @Test
    void getRequestsByUserTest() {
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(itemRequestRepository.findAllByUserId(anyLong()))
                .thenReturn(List.of(itemRequest));
        when(itemRepository.findAllByRequestId(anyLong()))
                .thenReturn(List.of(item));
        List<ItemResponseDto> itemResponsesDto = itemRequestService.getRequestsByUser(1);
        Assertions.assertEquals(itemResponsesDto.size(), 1);
        Assertions.assertEquals(itemResponsesDto.get(0).getItems().size(), 1);
        Assertions.assertEquals(itemResponsesDto.get(0).getId(), 1);
    }

    @Test
    void getRequestsByUserExceptionTest() {
        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> itemRequestService.getRequestsByUser(1));
        Assertions.assertEquals("Пользователь не найден", exception.getMessage());
    }

    @Test
    void getAllRequestsTest() {
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(itemRequestRepository.findAllByUserIdNot(anyLong(), any(Pageable.class)))
                .thenReturn(Page.empty());
        when(itemRepository.findAllByRequestId(anyLong()))
                .thenReturn(List.of());
        List<ItemResponseDto> itemResponseDto = itemRequestService.getAllRequests(1, 0, 10);
        Assertions.assertEquals(itemResponseDto.size(), 0);
    }

    @Test
    void getAllRequestsNotFoundExceptionTest() {
        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> itemRequestService.getAllRequests(1, 0, 10));
        Assertions.assertEquals("Пользователь не найден", exception.getMessage());
    }

    @Test
    void getAllRequestsBookingExceptionTest() {
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        final BookingException exception = Assertions.assertThrows(
                BookingException.class,
                () -> itemRequestService.getAllRequests(1, -1, 10));
        Assertions.assertEquals("Переданное значение меньше или равно нулю", exception.getMessage());
    }

    @Test
    void getRequestByRequestIdByUserTest() {
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        when(itemRequestRepository.existsById(anyLong()))
                .thenReturn(true);
        when(itemRequestRepository.getReferenceById(anyLong()))
                .thenReturn(itemRequest);
        when(itemRepository.findAllByRequestId(anyLong()))
                .thenReturn(List.of(item));
        ItemResponseDto itemResponseDto = itemRequestService.getRequestByRequestIdByUser(1, 1);
        Assertions.assertEquals(itemResponseDto.getItems().size(), 1);
        Assertions.assertEquals(itemResponseDto.getId(), 1);
        Assertions.assertNotNull(itemResponseDto.getCreated());
    }

    @Test
    void getRequestByRequestIdByUserNotFoundExceptionUserTest() {
        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> itemRequestService.getRequestByRequestIdByUser(1, 0));
        Assertions.assertEquals("Пользователь не найден", exception.getMessage());
    }

    @Test
    void getRequestByRequestIdByUserNotFoundExceptionItemRequestTest() {
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> itemRequestService.getRequestByRequestIdByUser(1, -1));
        Assertions.assertEquals("Запрос не найден", exception.getMessage());
    }
}
