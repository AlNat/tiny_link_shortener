package dev.alnat.tinylinkshortener.usecase;

import dev.alnat.tinylinkshortener.configuration.PostgreSQLTestContainerConfiguration;
import dev.alnat.tinylinkshortener.model.Link;
import dev.alnat.tinylinkshortener.model.enums.LinkStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.stream.Stream;

/**
 * Use case: usage of the available date.
 * Tests are not the ordering, just checking of few situation
 * <p>
 * Created by @author AlNat on 17.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@SpringBootTest
@ContextConfiguration(classes = PostgreSQLTestContainerConfiguration.class)
@AutoConfigureMockMvc
@DirtiesContext
class AvailableVisitUseCaseTest extends BaseMVCTest {

    private static final String FIRST_SHORT_LINK = "H4T"; // due 1_000_000 in ALPHABET

    private static final String REDIRECT_TO = "https://google.com/q=test";

    @AfterEach
    void clear() {
        visitRepository.deleteAll();
        linkRepository.deleteAll();
    }


    private static Stream<Arguments> availabilityCases() {
        return Stream.of(
                Arguments.of(null, null, true),
                Arguments.of(LocalDateTime.now().minusDays(1L), null, true),
                Arguments.of(null, LocalDateTime.now().plusDays(1L), true),
                Arguments.of(LocalDateTime.now().minusDays(1L), LocalDateTime.now().plusDays(1L), true),

                Arguments.of(LocalDateTime.now().minusDays(2L), LocalDateTime.now().minusDays(1L), false),
                Arguments.of(LocalDateTime.now().plusDays(1L), LocalDateTime.now().plusDays(2L), false),
                Arguments.of(LocalDateTime.now().plusDays(1L), null, false),
                Arguments.of(null, LocalDateTime.now().minusDays(1L), false)
        );
    };


    @ParameterizedTest
    @MethodSource("availabilityCases")
    void testAvailabilityLink(LocalDateTime from, LocalDateTime to,
                              boolean shouldBeRedirected) {
        var link = new Link();
        link.setAvailableFrom(from);
        link.setAvailableTo(to);
        link.setCreated(LocalDateTime.now());
        link.setOriginalLink(REDIRECT_TO);
        link.setShortLink(FIRST_SHORT_LINK);
        link.setStatus(LinkStatus.CREATED);

        linkRepository.save(link);

        if (shouldBeRedirected) {
            var redirectResult = redirect(FIRST_SHORT_LINK, true);

            Assertions.assertEquals(HttpStatus.FOUND.value(), redirectResult.getStatus(), "HTTP code is not correct!");
            Assertions.assertEquals(REDIRECT_TO, redirectResult.getRedirectedUrl(), "Redirect link is not the same that's created!");
        } else {
            var redirectResult = redirect(FIRST_SHORT_LINK, false);
            Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), redirectResult.getStatus(), "HTTP code is not correct! Should be not found");
        }
    }

}
