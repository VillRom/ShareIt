package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link ru.practicum.shareit.request.ItemRequest} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto implements Serializable {

    private long id;

    @NotNull
    @NotEmpty
    private String description;

    private LocalDateTime created;
}