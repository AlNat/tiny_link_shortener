package dev.alnat.tinylinkshortener.unit;

import dev.alnat.tinylinkshortener.dto.LinkInDTO;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for serialization of DTO
 * @see dev.alnat.tinylinkshortener.dto.LinkInDTO
 *
 * Created by @author AlNat on 21.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@JsonTest
class LinkItDTOMapperTest {

    @Autowired
    private JacksonTester<LinkInDTO> tester;

    @Test
    @SneakyThrows
    void serializationTest() {
        var dto = generateTestDTO();

        var json = tester.write(dto);

        assertThat(json).isEqualToJson("/model/link_in_dto.json");
    }

    @Test
    @SneakyThrows
    void deserializationTest() {
        var content = tester.readObject("/model/link_in_dto.json");
        var dto = generateTestDTO();

        assertThat(content).isEqualTo(dto);
    }

    private static LinkInDTO generateTestDTO() {
        var dto = new LinkInDTO();
        dto.setMaxVisitCount(1);
        dto.setOriginalLink("https://google.com?q=test");
        dto.setAvailableFrom(LocalDateTime.of(2023, 1, 1, 12, 0, 0));
        dto.setAvailableTo(LocalDateTime.of(2023, 1, 1, 13, 0, 0));
        return dto;
    }

}
