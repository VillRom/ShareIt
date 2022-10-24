package ru.practicum.shareit.validation;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.model.User;



@Component
public class Validation {

    public void validationUser(User user) throws ValidationException {
        if (user.getEmail() == null || !user.getEmail().contains("@") || user.getEmail().isEmpty()) {
            throw new ValidationException("Почта пустая или не указан символ - @");
        }
    }

    public void validationItemDto(ItemDto item) throws ValidationException {
        if (item.getName() == null || item.getName().isBlank()) {
            throw new ValidationException("Имя не может быть пустым");
        }
        if (item.getDescription() == null || item.getDescription().isBlank()) {
            throw new ValidationException("Описание не может быть пустым");
        }
        if (item.getAvailable() == null) {
            throw new ValidationException("Отсутствует статус");
        }
    }
}
