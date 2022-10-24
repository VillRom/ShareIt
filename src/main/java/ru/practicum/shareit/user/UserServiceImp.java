package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.validation.Validation;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;

    private final Validation validation;

    @Override
    public User addUser(User user) {
        if (userRepository.getAllUsers().stream().anyMatch(user1 -> user1.getEmail().equals(user.getEmail()))) {
            throw new EmailException("Такая почта: " + user.getEmail() + " уже использована");
        }
        validation.validationUser(user);
        return userRepository.saveUser(user);
    }

    @Override
    public User findUserById(long id) {
        if (userRepository.getUserById(id) == null) {
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
        return userRepository.getUserById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public User updateUserById(long userId, User user) {
        if (userRepository.getUserById(userId) == null) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
        if (userRepository.getAllUsers().stream().anyMatch(user1 -> user1.getEmail().equals(user.getEmail()))) {
            throw new EmailException("Такая почта: " + user.getEmail() + " уже использована");
        }
        return userRepository.updateUser(userId, user);
    }

    @Override
    public void deleteUserById(long id) {
        userRepository.deleteUserById(id);
    }
}
