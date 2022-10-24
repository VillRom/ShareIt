package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public List<ItemDto> getItemsFromUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getAllItemsFromUser(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemFromUser(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId) {
        return itemService.findItemById(userId, itemId);
    }

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody ItemDto itemDto) {
        return itemService.addItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody ItemDto itemDto,
                              @PathVariable long itemId) {
        return itemService.updateItemById(userId, itemDto, itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam String text) {
        return itemService.searchItems(userId, text);
    }
}
