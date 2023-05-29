package dev.alnat.tinylinkshortener.usecase;

import dev.alnat.tinylinkshortener.BaseMVCTest;
import dev.alnat.tinylinkshortener.E2ETest;
import dev.alnat.tinylinkshortener.dto.LinkInDTO;
import dev.alnat.tinylinkshortener.model.enums.LinkStatus;
import dev.alnat.tinylinkshortener.model.enums.VisitStatus;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static dev.alnat.tinylinkshortener.util.TestConstants.Link.REDIRECT_TO;

/**
 * Golden path of usage of API
 * Create link, go throw it and get statistics (should be saved)
 * <p>
 * Created by @author AlNat on 11.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@E2ETest
class GoldenPathUseCaseTest extends BaseMVCTest {

    private static String testShortLink;


    @Test
    @Order(1)
    @DisplayName("Step 1. Create the link")
    void create() {
        var link = new LinkInDTO();
        link.setOriginalLink(REDIRECT_TO);

        var result = saveNewLink(link);

        testShortLink = result.getData().getShortLink();

        Assertions.assertEquals(200, result.getCode(), "Result code is not success!");
        Assertions.assertTrue(result.getData().getCreated().isBefore(LocalDateTime.now()), "Link not immediately saved!");
        Assertions.assertEquals(LinkStatus.CREATED, result.getData().getStatus(), "Link status not as new!");
        Assertions.assertEquals(0, result.getData().getCurrentVisitCount(), "Link already visited!");
    }

    @Test
    @Order(2)
    @DisplayName("Step 2. Visit the link")
    void redirect() {
        var redirectResult = redirect(testShortLink, true);

        Assertions.assertEquals(HttpStatus.FOUND.value(), redirectResult.getStatus(), "HTTP code is not correct!");
        Assertions.assertEquals(REDIRECT_TO, redirectResult.getRedirectedUrl(), "Redirect link is not the same that's created!");
    }

    @Test
    @Order(3)
    @DisplayName("Step 3. Check visits log")
    void checkVisits() {
        var visitsResult = getVisits(testShortLink);

        Assertions.assertEquals(200, visitsResult.getCode(), "Result code is not success!");
        Assertions.assertEquals(1, visitsResult.getData().size(), "Visit must be alone!");

        var visit = visitsResult.getData().get(0);
        Assertions.assertTrue(visit.getVisitTime().isBefore(LocalDateTime.now()));
        Assertions.assertNotNull(visit.getIp());
        Assertions.assertNotNull(visit.getLink());
        Assertions.assertNotNull(visit.getUserAgent());
        Assertions.assertEquals(VisitStatus.SUCCESSFUL, visit.getStatus());

        Assertions.assertEquals(REDIRECT_TO, visit.getLink().getOriginalLink());
    }

    @Test
    @Order(4)
    void cleanUp() {
        clearDB();
    }

}
