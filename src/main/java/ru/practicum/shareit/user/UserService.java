package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    User addUser(User user);

    User findUserById(long id);

    List<User> getAllUsers();

    User updateUserById(long userId, User user);

    void deleteUserById(long id);
}
