package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
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
    public List<UserDto> getUserById() {
        return userService.getAllUsers();
    }

    @PostMapping()
    public UserDto saveUser(@RequestBody @Valid UserDto user) {
        return userService.addUser(user);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable long userId, @RequestBody UserDto user) {
        user.setId(userId);
        return userService.updateUserById(user);
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable long userId) {
        userService.deleteUserById(userId);
    }
}
