package dev.alnat.tinylinkshortener.unit;

import dev.alnat.tinylinkshortener.dto.LinkOutDTO;
import dev.alnat.tinylinkshortener.dto.VisitOutDTO;
import dev.alnat.tinylinkshortener.model.enums.LinkStatus;
import dev.alnat.tinylinkshortener.model.enums.VisitStatus;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for serialization of DTO
 * @see dev.alnat.tinylinkshortener.dto.VisitOutDTO
 *
 * Created by @author AlNat on 21.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@JsonTest
class VisitOutDTOMapperTest {

    @Autowired
    private JacksonTester<VisitOutDTO> tester;

    @Test
    @SneakyThrows
    void serializationTest() {
        var dto = generateTestDTO();

        var json = tester.write(dto);

        assertThat(json).isEqualToJson("/model/visit_out_dto.json");
    }

    @Test
    @SneakyThrows
    void deserializationTest() {
        var content = tester.readObject("/model/visit_out_dto.json");
        var dto = generateTestDTO();

        assertThat(content).isEqualTo(dto);
    }


    private static VisitOutDTO generateTestDTO() {
        var dto = new VisitOutDTO();
        dto.setIp("192.168.0.1");
        dto.setStatus(VisitStatus.EXPIRED);
        dto.setUserAgent("IE 6");
        dto.setVisitTime(LocalDateTime.of(2023, 1, 1, 12, 0, 1));


        var linkDto = new LinkOutDTO();
        linkDto.setShortLink("abc");
        linkDto.setStatus(LinkStatus.CREATED);
        linkDto.setId(123L);
        linkDto.setOriginalLink("https://google.com?q=test");
        linkDto.setCurrentVisitCount(1);
        linkDto.setCreated(LocalDateTime.of(2023, 1, 1, 12, 0, 0));

        dto.setLink(linkDto);

        return dto;
    }

}
