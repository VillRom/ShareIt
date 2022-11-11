package ru.practicum.shareit.item.model;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "item_name")
    private String name;

    @Column
    private String description;

    @Column
    private Boolean available;
}
