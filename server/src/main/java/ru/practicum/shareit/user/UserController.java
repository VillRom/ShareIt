package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable long userId) {
        return userService.findUserById(userId);
    }

    @GetMapping()
    public List<UserDto> getUsers() {
        return userService.getAllUsers();
    }

    @PostMapping()
    public UserDto saveUser(@RequestBody UserDto user) {
        return userService.addUser(user);
    }

    @PatchMapping
    public UserDto updateUser(@RequestBody UserDto user) {
        return userService.updateUserById(user);
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable long userId) {
        userService.deleteUserById(userId);
    }
}
