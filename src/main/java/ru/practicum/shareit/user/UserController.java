package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;
    private final MapperUser mapperUser;

    @Autowired
    public UserController(UserService userService, MapperUser mapperUser) {
        this.userService = userService;
        this.mapperUser = mapperUser;
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable long userId) {
        return mapperUser.userToUserDto(userService.findUserById(userId));
    }

    @GetMapping()
    public List<UserDto> getUserById() {
        return mapperUser.getUsersDtoFromUsers(userService.getAllUsers());
    }

    @PostMapping()
    public UserDto saveUser(@RequestBody User user) {
        return mapperUser.userToUserDto(userService.addUser(user));
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable long userId, @RequestBody User user) {
        return mapperUser.userToUserDto(userService.updateUserById(userId, user));
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable long userId) {
        userService.deleteUserById(userId);
    }
}
