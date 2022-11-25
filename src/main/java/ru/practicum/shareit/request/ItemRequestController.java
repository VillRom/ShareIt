package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemResponseDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto addRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                     @RequestBody @Valid ItemRequestDto itemRequestDto) {
        return itemRequestService.addRequest(userId, itemRequestDto);
    }

    @GetMapping
    public List<ItemResponseDto> getRequestsByUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestService.getRequestsByUser(userId);
    }

    @GetMapping("/{requestId}")
    public ItemResponseDto getRequestFromUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @PathVariable long requestId) {
        return itemRequestService.getRequestByRequestIdByUser(userId, requestId);
    }

    @GetMapping("/all")
    public List<ItemResponseDto> getAllRequests(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        return itemRequestService.getAllRequests(userId, from, size);
    }
}
