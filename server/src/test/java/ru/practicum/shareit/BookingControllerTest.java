package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {

    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    private final BookingResponseDto bookingResponseDto = new BookingResponseDto();

    private final BookingDto bookingDto = new BookingDto();

    @BeforeEach
    void setUp() {
        BookingResponseDto.Item itemBooking = new BookingResponseDto.Item(1, "name");
        BookingResponseDto.User userBooking = new BookingResponseDto.User(1, "user");
        bookingResponseDto.setId(1);
        bookingResponseDto.setStatus("WAITING");
        bookingResponseDto.setItem(itemBooking);
        bookingResponseDto.setBooker(userBooking);
        bookingDto.setId(1);
        bookingDto.setEnd(LocalDateTime.now().plusSeconds(60));
        bookingDto.setStart(LocalDateTime.now().plusSeconds(5));
        bookingDto.setBookerId(1);
        bookingDto.setItemId(1L);
    }

    @Test
    void addBookingTest() throws Exception {
        when(bookingService.addBooking(anyLong(), any()))
                .thenReturn(bookingResponseDto);
        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.status", is("WAITING")));
    }

    @Test
    void patchBookingTest() throws Exception {
        bookingResponseDto.setStatus("APPROVED");
        when(bookingService.updateStatusBooking(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingResponseDto);
        mvc.perform(patch("/bookings/{bookingId}", 1)
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.status", is("APPROVED")));
    }

    @Test
    void getBookingByIdTest() throws Exception {
        when(bookingService.getBookingByAuthorOrOwner(anyLong(), anyLong()))
                .thenReturn(bookingResponseDto);
        mvc.perform(get("/bookings/{bookingId}", 1)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.status", is("WAITING")));
    }

    @Test
    void getListBookingByUserTest() throws Exception {
        when(bookingService.getBookingsSort(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingResponseDto));
        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].status", is("WAITING")));
    }

    @Test
    void getListBookingByItemOwnerTest() throws Exception {
        when(bookingService.getBookingsByItemOwner(anyLong(),anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingResponseDto));
        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].status", is("WAITING")));
    }
}
