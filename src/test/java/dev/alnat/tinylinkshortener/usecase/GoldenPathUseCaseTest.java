package dev.alnat.tinylinkshortener.usecase;

import dev.alnat.tinylinkshortener.configuration.PostgreSQLTestContainerConfiguration;
import dev.alnat.tinylinkshortener.dto.LinkInDTO;
import dev.alnat.tinylinkshortener.model.enums.LinkStatus;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;

/**
 * Golden path of usage of API
 * Create link, go and get statistics
 * <p>
 * Created by @author AlNat on 11.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@SpringBootTest
@ContextConfiguration(classes = PostgreSQLTestContainerConfiguration.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Order for test
@AutoConfigureMockMvc
@DirtiesContext
class GoldenPathUseCaseTest extends BaseMVCTest {

    private static final String FIRST_SHORT_LINK = "H4T";

    private static final String REDIRECT_TO = "https://google.com/q=test";

    @Test
    @Order(1)
    @DisplayName("Step 1. Create the link")
    void create() {
        var link = new LinkInDTO();
        link.setOriginalLink(REDIRECT_TO);

        var result = saveNewLink(link);
        Assertions.assertEquals(200, result.getCode(), "Result code is not success!");
        Assertions.assertTrue(result.getData().getCreated().isBefore(LocalDateTime.now()), "Link not immediately saved!");
        Assertions.assertEquals(LinkStatus.CREATED, result.getData().getStatus(), "Link status not as new!");
        Assertions.assertEquals(0, result.getData().getCurrentVisitCount(), "Link already visited!");
        Assertions.assertEquals(FIRST_SHORT_LINK, result.getData().getShortLink(), "Short link not expected, is new engine?");
    }

    @Test
    @Order(2)
    @DisplayName("Step 2. Visit the link")
    void redirect() {
        Assertions.assertNotNull(redirect(FIRST_SHORT_LINK, true));
    }

    @Test
    @Order(3)
    @DisplayName("Step 3. Check visits")
    void checkVisits() {
        var visitsResult = getVisits(FIRST_SHORT_LINK);

        Assertions.assertEquals(200, visitsResult.getCode(), "Result code is not success!");
        Assertions.assertEquals(1, visitsResult.getData().size(), "Visit must be alone!");

        // TODO check visit in devitalise
    }

}
