package dev.alnat.tinylinkshortener.usecase;

import dev.alnat.tinylinkshortener.BaseMVCTest;
import dev.alnat.tinylinkshortener.E2ETest;
import dev.alnat.tinylinkshortener.util.LinkBuilder;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static dev.alnat.tinylinkshortener.util.TestConstants.Link.REDIRECT_TO;

/**
 * Use case: usage of the available date.
 * Tests are not the ordering, just checking of few situation
 * <p>
 * Created by @author AlNat on 17.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@E2ETest
@TestMethodOrder(MethodOrderer.Random.class) // Order for test
class AvailableVisitUseCaseTest extends BaseMVCTest {

    @AfterEach
    @BeforeEach
    void clear() {
        clearDB();
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
        var link = LinkBuilder.someLink()
                .withAvailableFrom(from)
                .withAvailableTo(to)
                .build();

        linkRepository.save(link);

        if (shouldBeRedirected) {
            var redirectResult = redirect(link.getShortLink(), true);

            Assertions.assertEquals(HttpStatus.FOUND.value(), redirectResult.getStatus(), "HTTP code is not correct!");
            Assertions.assertEquals(REDIRECT_TO, redirectResult.getRedirectedUrl(), "Redirect link is not the same that's created!");
        } else {
            var redirectResult = redirect(link.getShortLink(), false);
            Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), redirectResult.getStatus(), "HTTP code is not correct! Should be not found");
        }
    }

}
