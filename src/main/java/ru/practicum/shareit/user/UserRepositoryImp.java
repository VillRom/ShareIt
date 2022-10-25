package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.EmailException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryImp implements UserRepository {

    private long id = 1;
    private final Map<Long, User> storageUser = new HashMap<>();

    @Override
    public User saveUser(User user) {
        if (storageUser.values().stream().anyMatch(user1 -> user1.getEmail().equals(user.getEmail()))) {
            throw new EmailException("Такая почта: " + user.getEmail() + " уже использована");
        }
        user.setId(id);
        id++;
        storageUser.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUserById(long userId) {
        return storageUser.get(userId);
    }

    @Override
    public User updateUser(User user) {
        if (storageUser.values().stream().anyMatch(user1 -> user1.getEmail().equals(user.getEmail()))) {
            throw new EmailException("Такая почта: " + user.getEmail() + " уже использована");
        }
        User userUpdate = getUserById(user.getId());
        if (!userUpdate.getName().equals(user.getName()) && user.getName() != null) {
            userUpdate.setName(user.getName());
        }
        if (!userUpdate.getEmail().equals(user.getEmail()) && user.getEmail() != null) {
            userUpdate.setEmail(user.getEmail());
        }
        storageUser.put(user.getId(), userUpdate);
        return userUpdate;
    }

    @Override
    public void deleteUserById(long userId) {
        storageUser.remove(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(storageUser.values());
    }
}
