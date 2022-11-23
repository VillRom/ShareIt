package ru.practicum.shareit.request;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@Entity
@Table(name = "requests")
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String description;

    @Column
    private LocalDateTime created;

    @Column(name = "user_id")
    private long userId;
}
