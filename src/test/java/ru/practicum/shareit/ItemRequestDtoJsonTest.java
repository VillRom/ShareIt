package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;


@JsonTest
public class ItemRequestDtoJsonTest {

    @Autowired
    private JacksonTester<ItemRequestDto> json;

    private final LocalDateTime localTime = LocalDateTime.of(2022, 11, 10, 10 ,10 ,0);

    @Test
    void testSerialize() throws Exception {
        ItemRequestDto itemRequestDto = new ItemRequestDto(1, "description",
                localTime);
        JsonContent<ItemRequestDto> result = json.write(itemRequestDto);
        Assertions.assertEquals(result.getJson(), "{\"id\":1,\"description\":\"description\",\"created\":\"" +
                "2022-11-10T10:10:00\"}");
    }

    @Test
    void testDeserialize() throws Exception {
        String makeJson = "{\"id\":1,\"description\":\"description\",\"created\":\"" +
                "2022-11-10T10:10:00\"}";
        ItemRequestDto itemRequestDto = json.parseObject(makeJson);
        Assertions.assertEquals(itemRequestDto.getId(), 1);
    }
}
