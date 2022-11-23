package ru.practicum.shareit.request;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findAllByUserId(long userId);


    Page<ItemRequest> findAllByUserIdNot(long userId, @NonNull Pageable pageable);
}
