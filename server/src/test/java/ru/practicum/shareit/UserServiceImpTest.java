package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.UserServiceImp;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


@Transactional
@SpringBootTest
public class UserServiceImpTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    private final User user = new User();

    private final UserDto userDto = new UserDto();

    private final List<User> users = new ArrayList<>();


    @BeforeEach
    void makeService() {
        userService = new UserServiceImp(userRepository);
        user.setEmail("yandex@yandex.ru");
        user.setName("Test");
        user.setId(1);
        userDto.setEmail("yandex@yandex.ru");
        userDto.setName("Test");
        userDto.setId(1);
        User user1 = new User();
        user1.setEmail("yandex1@yandex.ru");
        user1.setName("Test1");
        user1.setId(2);
        users.add(user);
        users.add(user1);
    }

    @Test
    void saveUser() {
        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(user);
        UserDto userDtoTest = userService.addUser(userDto);
        Assertions.assertEquals(userDtoTest, userDto);
    }

    @Test
    void findUserByIdTest() {
        Mockito.when(userRepository.getReferenceById(Mockito.anyLong()))
                .thenReturn(user);
        Mockito.when(userRepository.existsById(Mockito.anyLong()))
                .thenReturn(true);
        UserDto userDtoTest = userService.findUserById(1);
        Assertions.assertEquals(userDtoTest, userDto);
    }

    @Test
    void findUserByIdTestException() {
        Mockito.when(userRepository.existsById(Mockito.anyLong()))
                .thenReturn(false);
        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> userService.findUserById(1));
        Assertions.assertEquals("Пользователь с id = 1 не найден", exception.getMessage());
    }

    @Test
    void findAllTest() {
        Mockito.when(userRepository.findAll())
                .thenReturn(users);
        List<UserDto> usersDto = userService.getAllUsers();
        Assertions.assertEquals(usersDto.size(), 2);
        Assertions.assertEquals(usersDto.get(0).getId(), 1);
    }

    @Test
    void updateNameUser() {
        UserDto userDto1 = new UserDto();
        userDto1.setName("NameTest");
        User userUpdate = new User();
        userUpdate.setId(1);
        userUpdate.setEmail(user.getEmail());
        userUpdate.setName("NameTest");
        Mockito.when(userRepository.existsById(Mockito.anyLong()))
                .thenReturn(true);
        Mockito.when(userRepository.getReferenceById(Mockito.anyLong()))
                .thenReturn(user);
        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(userUpdate);
        UserDto userDtoTest = userService.updateUserById(userDto1);
        Assertions.assertEquals(userDtoTest.getName(), "NameTest");
    }

    @Test
    void updateEmailUser() {
        UserDto userDto1 = new UserDto();
        userDto1.setEmail("yandexTest@yandex.ru");
        User userUpdate = new User();
        userUpdate.setId(1);
        userUpdate.setEmail("yandexTest@yandex.ru");
        userUpdate.setName(user.getName());
        Mockito.when(userRepository.existsById(Mockito.anyLong()))
                .thenReturn(true);
        Mockito.when(userRepository.getReferenceById(Mockito.anyLong()))
                .thenReturn(user);
        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(userUpdate);
        UserDto userDtoTest = userService.updateUserById(userDto1);
        Assertions.assertEquals(userDtoTest.getEmail(), "yandexTest@yandex.ru");
    }

    @Test
    void updateUserException() {
        Mockito.when(userRepository.existsById(Mockito.anyLong()))
                .thenReturn(false);
        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> userService.updateUserById(userDto));
        Assertions.assertEquals("Пользователь с id = 1 не найден", exception.getMessage());
    }

    @Test
    void deleteUserTest() {
        userService.deleteUserById(1);
        Mockito.verify(userRepository, Mockito.times(1))
                .deleteById(1L);
    }
}
