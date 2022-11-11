package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link ru.practicum.shareit.item.comment.Comment} entity
 */
@Data
public class CommentDto implements Serializable {

    private long id;

    @NotEmpty
    @NotNull
    private String text;

    private String authorName;

    private LocalDateTime created;
}