package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemResponseDto;

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

@WebMvcTest(controllers = ItemRequestController.class)
public class ItemRequestControllerTest {

    @MockBean
    private ItemRequestService itemRequestService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    private ItemRequestDto itemRequestDto;

    private ItemResponseDto itemResponseDto;

    @BeforeEach
    void setUp() {
        ItemResponseDto.Item item = new ItemResponseDto.Item(1L, "item", "description", true,
                1L);
        itemRequestDto = new ItemRequestDto(1,"Description", LocalDateTime.now());
        itemResponseDto = new ItemResponseDto(1, "Description", LocalDateTime.now(), List.of(item));
    }

    @Test
    void addRequestTest() throws Exception {
        when(itemRequestService.addRequest(anyLong(), any()))
                .thenReturn(itemRequestDto);
        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())));
    }

    @Test
    void getRequestsByUserTest() throws Exception {
        when(itemRequestService.getRequestsByUser(anyLong()))
                .thenReturn(List.of(itemResponseDto));
        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].items", hasSize(1)))
                .andExpect(jsonPath("$[0].description", is(itemResponseDto.getDescription())));
    }

    @Test
    void getRequestFromUserTest() throws Exception {
        when(itemRequestService.getRequestByRequestIdByUser(anyLong(), anyLong()))
                .thenReturn(itemResponseDto);
        mvc.perform(get("/requests/{requestId}", 1L)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.description", is(itemResponseDto.getDescription())));
    }

    @Test
    void getAllRequestsTest() throws Exception {
        when(itemRequestService.getAllRequests(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(itemResponseDto));
        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].items", hasSize(1)))
                .andExpect(jsonPath("$[0].description", is(itemResponseDto.getDescription())));
    }
}
