package ru.practicum.shareit.user.dto;

import lombok.Data;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class UserDto {

    private long id;

    @NotNull
    @Email
    private String email;

    private String name;
}
