package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto addUser(UserDto user) {
        return UserMapper.userToUserDto(userRepository.save(UserMapper.userDtoToUser(user)));
    }

    @Override
    public UserDto findUserById(long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
        return UserMapper.userToUserDto(userRepository.getReferenceById(id));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return UserMapper.getUsersDtoFromUsers(userRepository.findAll());
    }

    @Override
    @Transactional
    public UserDto updateUserById(UserDto user) {
        if (!userRepository.existsById(user.getId())) {
            throw new NotFoundException("Пользователь с id = " + user.getId() + " не найден");
        }
        User userUpdate = userRepository.getReferenceById(user.getId());
        if (!userUpdate.getName().equals(user.getName()) && user.getName() != null) {
            userUpdate.setName(user.getName());
        }
        if (!userUpdate.getEmail().equals(user.getEmail()) && user.getEmail() != null) {
            userUpdate.setEmail(user.getEmail());
        }
        return UserMapper.userToUserDto(userRepository.save(userUpdate));
    }

    @Override
    @Transactional
    public void deleteUserById(long id) {
        userRepository.deleteById(id);
    }
}
