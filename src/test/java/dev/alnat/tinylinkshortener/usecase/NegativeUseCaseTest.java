package dev.alnat.tinylinkshortener.usecase;

import dev.alnat.tinylinkshortener.E2ETest;
import dev.alnat.tinylinkshortener.configuration.PostgreSQLTestContainerConfiguration;
import dev.alnat.tinylinkshortener.dto.LinkOutDTO;
import dev.alnat.tinylinkshortener.dto.common.Result;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

/**
 * Some negative cases
 * <p>
 * Created by @author AlNat on 17.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@E2ETest
class NegativeUseCaseTest extends BaseMVCTest {

    @Test
    void testRedirectOfNotFoundLink() {
        var redirectResult = redirect("abc", false);
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), redirectResult.getStatus(), "HTTP code is not correct! Should be not found");
    }

    @Test
    void testNotFoundStatistics() {
        var visitsResult = getVisits("abc");
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), visitsResult.getCode(), "Result code is not expected!!");
    }

    @ValueSource(
            strings = {
                    "",
                    "{}",
                    "{",
                    // not present short link
                    """
                    {
                      "availableFrom": "2023-01-01 12:00:00",
                      "availableTo": "2023-01-01 13:00:00",
                      "maxVisitCount": 1
                    }
                    """,
                    // illegal date format
                    """
                    {
                      "originalLink": "https://google.com?q=test",
                      "availableFrom": "2023-01-01A12:00:00",
                      "availableTo": "2023-01-01 13:00:00",
                      "maxVisitCount": 1
                    }
                    """
            }
    )
    @ParameterizedTest
    @DisplayName("Illegal DTO in short link creation")
    void illegalDTOCreating(String dto) {
        String response = syncResponseToPOST("/api/v1/link/", dto);

        Result<LinkOutDTO> res;
        try {
            res = mapper.readValue(response, LINK_RESPONSE_TYPE);
        } catch (Exception e) {
            Assertions.fail(e);
            return;
        }

        Assertions.assertEquals(400, res.getCode());
    }

}
