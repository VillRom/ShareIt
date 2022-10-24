package ru.practicum.shareit.item.model;

import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class Item {

    private long id;

    private long userId;

    private String name;

    private String description;

    private Boolean available;
}
