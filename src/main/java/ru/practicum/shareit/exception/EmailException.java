package ru.practicum.shareit.exception;

import org.springframework.dao.DataIntegrityViolationException;

public class EmailException extends DataIntegrityViolationException {

    public EmailException(String message) {
        super(message);
    }
}
