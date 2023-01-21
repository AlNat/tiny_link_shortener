package dev.alnat.tinylinkshortener.usecase;

import dev.alnat.tinylinkshortener.configuration.PostgreSQLTestContainerConfiguration;
import dev.alnat.tinylinkshortener.dto.LinkInDTO;
import dev.alnat.tinylinkshortener.model.enums.LinkStatus;
import dev.alnat.tinylinkshortener.model.enums.VisitStatus;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;

/**
 * Use case: usage of the limit of visits.
 * Create link with few visit, visit link limit + 1 times, fetch stats: all should be success, last -- out of limit
 * <p>
 * Created by @author AlNat on 17.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@SpringBootTest
@ContextConfiguration(classes = PostgreSQLTestContainerConfiguration.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Order for test
@AutoConfigureMockMvc
@DirtiesContext
class LimitVisitUseCaseTest extends BaseMVCTest {

    private static final Integer MAX_VISIT_COUNT = 2;

    private static final String FIRST_SHORT_LINK = "H4T"; // due 1_000_000 in ALPHABET

    private static final String REDIRECT_TO = "https://google.com/q=test";

    @Test
    @Order(1)
    @DisplayName("Step 1. Create the link with max visit count")
    void create() {
        var link = new LinkInDTO();
        link.setOriginalLink(REDIRECT_TO);
        link.setMaxVisitCount(MAX_VISIT_COUNT);

        var result = saveNewLink(link);
        Assertions.assertEquals(200, result.getCode(), "Result code is not success!");
        Assertions.assertTrue(result.getData().getCreated().isBefore(LocalDateTime.now()), "Link not immediately saved!");
        Assertions.assertEquals(LinkStatus.CREATED, result.getData().getStatus(), "Link status not as new!");
        Assertions.assertEquals(0, result.getData().getCurrentVisitCount(), "Link already visited!");
        Assertions.assertEquals(FIRST_SHORT_LINK, result.getData().getShortLink(), "Short link not expected, is new engine?");
    }

    @Test
    @Order(2)
    @DisplayName("Step 2. Visit the link max count")
    void visitBeforeLimit() {
        for (int count = 0; count < MAX_VISIT_COUNT; count++) {
            var redirectResult = redirect(FIRST_SHORT_LINK, true);

            Assertions.assertEquals(HttpStatus.FOUND.value(), redirectResult.getStatus(), "HTTP code is not correct!");
            Assertions.assertEquals(REDIRECT_TO, redirectResult.getRedirectedUrl(), "Redirect link is not the same that's created!");
        }
    }

    @Test
    @Order(3)
    @DisplayName("Step 3. Visit the link after the limit exceeded")
    void visitAfterLimit() {
        var redirectResult = redirect(FIRST_SHORT_LINK, false);

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), redirectResult.getStatus(), "HTTP code is not correct! Should be not found");
    }

    @Test
    @Order(4)
    @DisplayName("Step 4. Check visits log")
    void checkVisits() {
        var visitsResult = getVisits(FIRST_SHORT_LINK);

        Assertions.assertEquals(200, visitsResult.getCode(), "Result code is not success!");
        Assertions.assertEquals(MAX_VISIT_COUNT + 1, visitsResult.getData().size(), "Visit must be max + 1!");

        // Check all visits have the mandatory data
        Assertions.assertTrue(visitsResult.getData().stream().allMatch(v -> v.getVisitTime().isBefore(LocalDateTime.now())));
        Assertions.assertTrue(visitsResult.getData().stream().allMatch(v -> v.getIp() != null));
        Assertions.assertTrue(visitsResult.getData().stream().allMatch(v -> v.getLink() != null));
        Assertions.assertTrue(visitsResult.getData().stream().allMatch(v -> v.getUserAgent() != null));
        Assertions.assertTrue(visitsResult.getData().stream().allMatch(v -> v.getLink().getOriginalLink().equals(REDIRECT_TO)));

        // Check statuses of visits: one should be over limit
        Assertions.assertEquals(MAX_VISIT_COUNT.longValue(), visitsResult.getData().stream()
                .filter(v -> v.getStatus().equals(VisitStatus.SUCCESSFUL)).count());
        Assertions.assertEquals(1L, visitsResult.getData().stream()
                .filter(v -> v.getStatus().equals(VisitStatus.TOO_MUCH_REQUEST)).count());
    }

}
