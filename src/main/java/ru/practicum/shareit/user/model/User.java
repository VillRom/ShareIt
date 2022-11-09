package ru.practicum.shareit.user.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Email
    @Column
    private String email;

    @Column(name = "user_name")
    private String name;
}
