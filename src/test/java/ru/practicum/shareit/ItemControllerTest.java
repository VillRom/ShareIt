package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithDateBooking;

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

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {

    @MockBean
    ItemService itemService;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    private final ItemDto itemDto = new ItemDto();

    private final CommentDto commentDto = new CommentDto();

    private final ItemWithDateBooking itemWithDateBooking = new ItemWithDateBooking();

    @BeforeEach
    void setUp() {
        itemDto.setId(1);
        itemDto.setDescription("description");
        itemDto.setName("name");
        itemDto.setAvailable(true);
        itemDto.setRequestId(1L);
        commentDto.setId(1);
        commentDto.setAuthorName("author name");
        commentDto.setText("text");
        commentDto.setCreated(LocalDateTime.now());
        ItemWithDateBooking.Comment comment = new ItemWithDateBooking.Comment(1, "text",
                "author name", LocalDateTime.now());
        itemWithDateBooking.setId(1);
        itemWithDateBooking.setAvailable(true);
        itemWithDateBooking.setName("name");
        itemWithDateBooking.setComments(List.of(comment));
    }

    @Test
    void getItemsFromUserTest() throws Exception {
        when(itemService.getAllItemsFromUser(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(itemWithDateBooking));
        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemWithDateBooking.getId()), Long.class));
    }

    @Test
    void getItemFromUserTest() throws Exception {
        when(itemService.findItemById(anyLong(), anyLong()))
                .thenReturn(itemWithDateBooking);
        mvc.perform(get("/items/{itemId}", 1)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.available", is(true)));
    }

    @Test
    void getItemFromUserStatusTest() throws Exception {
        when(itemService.findItemById(anyLong(), anyLong()))
                .thenThrow(new NotFoundException("Пользователь не найден"));
        mvc.perform(get("/items/{itemId}", 1)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void addItemTest() throws Exception {
        when(itemService.addItem(anyLong(), any()))
                .thenReturn(itemDto);
        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("name")));
    }

    @Test
    void updateItemTest() throws Exception {
        ItemDto itemDtoTest = new ItemDto();
        itemDtoTest.setName("test");
        itemDto.setName("test");
        when(itemService.updateItemById(anyLong(), any(), anyLong()))
                .thenReturn(itemDto);
        mvc.perform(patch("/items/{itemId}", 1)
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemDtoTest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("test")));
    }

    @Test
    void searchItemsTest() throws Exception {
        when(itemService.searchItems(anyLong(), anyString()))
                .thenReturn(List.of(itemDto));
        mvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 1L)
                        .param("text", "description"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("name")));
    }

    @Test
    void addCommentTest() throws Exception {
        when(itemService.addComment(anyLong(), anyLong(), any()))
                .thenReturn(commentDto);
        mvc.perform(post("/items/{itemId}/comment", 1)
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.authorName", is("author name")));
    }
}
