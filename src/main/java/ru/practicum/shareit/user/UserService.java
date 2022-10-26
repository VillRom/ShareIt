package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto addUser(UserDto user);

    UserDto findUserById(long id);

    List<UserDto> getAllUsers();

    UserDto updateUserById(UserDto user);

    void deleteUserById(long id);
}
