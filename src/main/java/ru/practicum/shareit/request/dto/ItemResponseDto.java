package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * A DTO for the {@link ru.practicum.shareit.request.ItemRequest} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponseDto {
    private long id;
    private String description;
    private LocalDateTime created;
    private List<Item> items;

    @Data
    public static class Item {
        private Long id;
        private String name;
        private String description;
        private Boolean available;
        private Long requestId;

        public Item(Long id, String name, String description, Boolean available, Long requestId) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.available = available;
            this.requestId = requestId;
        }
    }
}