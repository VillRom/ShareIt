package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {

    User saveUser(User user);

    User getUserById(long userId);

    User updateUser(long userId, User user);

    void deleteUserById(long userId);

    List<User> getAllUsers();
}
