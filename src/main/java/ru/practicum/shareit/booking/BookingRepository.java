package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Page<Booking> findAllByBooker_IdOrderByStartDesc(long userId, Pageable pageable);

    Page<Booking> findAllByBooker_IdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(long userId, LocalDateTime dateTime,
                                                                                  LocalDateTime dateTime1,
                                                                                  Pageable pageable);

    List<Booking> findAllByItem_IdAndBooker_IdAndEndIsBefore(long itemId, long userId, LocalDateTime dateTime);

    Page<Booking> findAllByBooker_IdAndStartIsAfterOrderByStartDesc(long userId, LocalDateTime dateTime,
                                                                    Pageable pageable);

    Page<Booking> findAllByBooker_IdAndEndIsBeforeOrderByStartDesc(long userId, LocalDateTime dateTime,
                                                                   Pageable pageable);

    Page<Booking> findAllByBooker_IdAndStatusContainingOrderByStartDesc(long userId, String status, Pageable pageable);

    Page<Booking> findAllByItem_UserIdOrderByStartDesc(long ownerId, Pageable pageable);
    //findAllByItem_UserIdOrderByStartDesc

    Page<Booking> findAllByItem_UserIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(long ownerId,
                                                                                    LocalDateTime dateTime,
                                                                                    LocalDateTime dateTime1,
                                                                                    Pageable pageable);

    Page<Booking> findAllByItem_UserIdAndStartIsAfterOrderByStartDesc(long ownerId, LocalDateTime dateTime,
                                                                      Pageable pageable);

    Page<Booking> findAllByItem_UserIdAndEndIsBeforeOrderByStartDesc(long ownerId, LocalDateTime dateTime,
                                                                     Pageable pageable);

    Page<Booking> findAllByItem_UserIdAndStatusContainingOrderByStartDesc(long ownerId, String state, Pageable pageable);

    List<Booking> findAllByItem_IdAndItem_UserId(long id, long userId);
}
