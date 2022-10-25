package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto addUser(UserDto user) {
        return UserMapper.userToUserDto(userRepository.saveUser(UserMapper.userDtoToUser(user)));
    }

    @Override
    public UserDto findUserById(long id) {
        if (userRepository.getUserById(id) == null) {
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
        return UserMapper.userToUserDto(userRepository.getUserById(id));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return UserMapper.getUsersDtoFromUsers(userRepository.getAllUsers());
    }

    @Override
    public UserDto updateUserById(UserDto user) {
        if (userRepository.getUserById(user.getId()) == null) {
            throw new NotFoundException("Пользователь с id = " + user.getId() + " не найден");
        }
        return UserMapper.userToUserDto(userRepository.updateUser(UserMapper.userDtoToUser(user)));
    }

    @Override
    public void deleteUserById(long id) {
        userRepository.deleteUserById(id);
    }
}
