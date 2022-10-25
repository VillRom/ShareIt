package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ItemDto {

    private long id;

    @NotEmpty
    @NotNull
    private String name;

    @NotNull
    @NotEmpty
    private String description;

    @NotNull
    private Boolean available;
}
