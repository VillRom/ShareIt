package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBooker_IdOrderByStartDesc(long userId);

    List<Booking> findAllByBooker_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(long userId, LocalDateTime dateTime,
                                                                                  LocalDateTime dateTime1);

    List<Booking> findAllByItem_IdAndBooker_IdAndEndIsBefore(long itemId, long userId, LocalDateTime dateTime);

    List<Booking> findAllByBooker_IdAndStartIsAfterOrderByStartDesc(long userId, LocalDateTime dateTime);

    List<Booking> findAllByBooker_IdAndEndIsBeforeOrderByStartDesc(long userId, LocalDateTime dateTime);

    List<Booking> findAllByBooker_IdAndStatusContainingOrderByStartDesc(long userId, String status);

    List<Booking> findAllByItem_UserIdOrderByStartDesc(long ownerId);

    List<Booking> findAllByItem_UserIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(long ownerId,
                                                                                    LocalDateTime dateTime,
                                                                                    LocalDateTime dateTime1);

    List<Booking> findAllByItem_UserIdAndStartIsAfterOrderByStartDesc(long ownerId, LocalDateTime dateTime);

    List<Booking> findAllByItem_UserIdAndEndIsBeforeOrderByStartDesc(long ownerId, LocalDateTime dateTime);

    List<Booking> findAllByItem_UserIdAndStatusContainingOrderByStartDesc(long ownerId, String state);

    List<Booking> findAllByItem_IdAndItem_UserId(long id, long userId);
}
