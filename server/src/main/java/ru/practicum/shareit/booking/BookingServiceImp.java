package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.exception.BookingException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserRepository;

import javax.persistence.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImp implements BookingService {

    private final BookingRepository bookingRepository;

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    @Override
    @Transactional
    public BookingResponseDto addBooking(long userId, BookingDto booking) {
        if (!itemRepository.getReferenceById(booking.getItemId()).getAvailable()) {
            throw new BookingException("Бронорование не доступно");
        }
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        if (!itemRepository.existsById(booking.getItemId()) ||
                itemRepository.getReferenceById(booking.getItemId()).getUserId() == userId) {
            throw new EntityNotFoundException("Вещь не найдена");
        }
        return BookingMapper.bookingToBookingResponseDto(bookingRepository.save(BookingMapper.bookingDtoToBooking(booking,
                itemRepository.getReferenceById(booking.getItemId()), userRepository.getReferenceById(userId))));
    }

    @Override
    public BookingResponseDto getBookingByAuthorOrOwner(long authorId, long bookingId) {
        if (!userRepository.existsById(authorId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        if (authorId != bookingRepository.getReferenceById(bookingId).getItem().getUserId()
                && authorId != bookingRepository.getReferenceById(bookingId).getBooker().getId()) {
            throw new NotFoundException("В доступе отказано");
        }
        return BookingMapper.bookingToBookingResponseDto(bookingRepository.getReferenceById(bookingId));
    }

    @Override
    public List<BookingResponseDto> getBookingsSort(long authorId, String state, int from, int size) {
        if (!userRepository.existsById(authorId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        if (from <= 0 || size <= 0) {
            throw new BookingException("Переданное значение меньше или равно нулю");
        }
        LocalDateTime dateTime = LocalDateTime.now();
        switch (state) {
            case "ALL":
                return BookingMapper.bookingsToBookingResponseDtoList(bookingRepository
                        .findAllByBooker_IdOrderByStartDesc(authorId, PageRequest.of(from - 1, size)).getContent());
            case "CURRENT":
                return BookingMapper.bookingsToBookingResponseDtoList(bookingRepository
                        .findAllByBooker_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(authorId, dateTime, dateTime,
                                PageRequest.of(from - 1, size)).getContent());
            case "FUTURE":
                return BookingMapper.bookingsToBookingResponseDtoList(bookingRepository
                        .findAllByBooker_IdAndStartIsAfterOrderByStartDesc(authorId, dateTime,
                                PageRequest.of(from - 1, size)).getContent());
            case "PAST":
                return BookingMapper.bookingsToBookingResponseDtoList(bookingRepository
                        .findAllByBooker_IdAndEndIsBeforeOrderByStartDesc(authorId, dateTime,
                                PageRequest.of(from - 1, size)).getContent());
            case "WAITING":
            case "REJECTED":
                return BookingMapper.bookingsToBookingResponseDtoList(bookingRepository
                        .findAllByBooker_IdAndStatusContainingOrderByStartDesc(authorId, state,
                                PageRequest.of(from - 1, size)).getContent());
            default:
                throw new BookingException("Unknown state: " + state);
        }
    }

    @Override
    public List<BookingResponseDto> getBookingsByItemOwner(long ownerId, String state, int from, int size) {
        if (!userRepository.existsById(ownerId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        if (from < 0 || size <= 0) {
            throw new BookingException("Переданное значение меньше или равно нулю");
        }
        LocalDateTime dateTime = LocalDateTime.now();
        switch (state) {
            case "ALL":
                return BookingMapper.bookingsToBookingResponseDtoList(bookingRepository
                        .findAllByItem_UserIdOrderByStartDesc(ownerId, PageRequest.of(from, size,
                                Sort.by("start").descending())).getContent());
            case "CURRENT":
                return BookingMapper.bookingsToBookingResponseDtoList(bookingRepository
                        .findAllByItem_UserIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(ownerId, dateTime, dateTime,
                                PageRequest.of(from, size)).getContent());
            case "FUTURE":
                return BookingMapper.bookingsToBookingResponseDtoList(bookingRepository
                        .findAllByItem_UserIdAndStartIsAfterOrderByStartDesc(ownerId, dateTime,
                                PageRequest.of(from, size)).getContent());
            case "PAST":
                return BookingMapper.bookingsToBookingResponseDtoList(bookingRepository
                        .findAllByItem_UserIdAndEndIsBeforeOrderByStartDesc(ownerId, dateTime,
                                PageRequest.of(from, size)).getContent());
            case "WAITING":
            case "REJECTED":
                return BookingMapper.bookingsToBookingResponseDtoList(bookingRepository
                        .findAllByItem_UserIdAndStatusContainingOrderByStartDesc(ownerId, state,
                                PageRequest.of(from, size)).getContent());
            default:
                throw new BookingException("Unknown state: " + state);
        }
    }

    @Override
    @Transactional
    public BookingResponseDto updateStatusBooking(long authorId, long bookingId, boolean approved) {
        Booking booking = bookingRepository.getReferenceById(bookingId);
        if (booking.getItem().getUserId() != authorId) {
            throw new NotFoundException("Вещь не найдена");
        }
        if (!booking.getStatus().equals("WAITING")) {
            throw new BookingException("Статус запроса уже изменен");
        }
        if (approved) {
            booking.setStatus("APPROVED");
        } else {
            booking.setStatus("REJECTED");
        }
        return BookingMapper.bookingToBookingResponseDto(bookingRepository.save(booking));
    }
}
