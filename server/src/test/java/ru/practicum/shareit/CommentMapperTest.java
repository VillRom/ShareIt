package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class CommentMapperTest {

    private final Comment comment = new Comment();

    private final Item item = new Item();

    private final User user = new User();

    @BeforeEach
    void setUp() {
        item.setId(1L);
        item.setUserId(1);
        item.setName("name");
        item.setRequestId(1L);
        item.setAvailable(true);
        item.setDescription("description");
        user.setId(1L);
        user.setName("name");
        user.setEmail("name@mail.ru");
        comment.setId(1);
        comment.setText("text");
        comment.setCreated(LocalDateTime.now());
        comment.setItem(item);
        comment.setAuthor(user);
    }

    @Test
    void commentsToCommentDtoListTest() {
        List<CommentDto> commentsDto = CommentMapper.commentsToCommentDtoList(List.of(comment));
        Assertions.assertEquals(commentsDto.size(), 1);
        Assertions.assertEquals(commentsDto.get(0).getId(), 1);
        Assertions.assertEquals(commentsDto.get(0).getAuthorName(), "name");
    }
}
