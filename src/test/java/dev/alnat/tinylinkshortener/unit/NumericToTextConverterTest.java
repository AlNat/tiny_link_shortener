package dev.alnat.tinylinkshortener.unit;

import dev.alnat.tinylinkshortener.engine.NumericToTextConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.stream.Stream;

/**
 * Created by @author AlNat on 11.01.2023.
 * Licensed by Apache License, Version 2.0
 */
class NumericToTextConverterTest {

    private static Stream<Arguments> generalTestCases() {
        return Stream.of(
                Arguments.of(1L, "a"),
                Arguments.of(2L, "b"),
                Arguments.of(55L, "-"),
                Arguments.of(56L, "a_"),
                Arguments.of(58L, "ab"),
                Arguments.of(100L, "aX"),
                Arguments.of(1_000L, "t3"),
                Arguments.of(2_737L, "34"),
                Arguments.of(5_000L, "aKs"),
                Arguments.of(1_000_000L, "eR4h"),
                Arguments.of(3_002_065L, "test"),
                Arguments.of(9_834_497L, "a___a"),
                Arguments.of(100_000_000L, "kjzVs"),
                Arguments.of(123_456_789L, "nG-KF"),
                Arguments.of(Long.MAX_VALUE - 1L, "Gy7n7nW9nUf"),
                Arguments.of(Long.MAX_VALUE, "Gy7n7nW9nUg") // our last short link
        );
    };

    @ParameterizedTest
    @MethodSource("generalTestCases")
    @DisplayName("Basic conversion test")
    void conversionTest(final long id, final String code) {
        // creation test
        Assertions.assertEquals(
                code, NumericToTextConverter.generateShortLink(id),
                "Conversion is not passed, did algo was changed?"
        );

        // and backward test
        Assertions.assertEquals(
                id, NumericToTextConverter.generateIdFromShortLink(code),
                "Conversion is not passed, did algo was changed?"
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "abc",
            "_abc",
            "__abc"
    })
    @DisplayName("Test that trailing zeros in short-based alphabetic is ignored")
    void trailingZeroAlternativeTest(final String code) {
        final long id = 3251L;

        Assertions.assertEquals(
                id, NumericToTextConverter.generateIdFromShortLink(code),
                "Trailing zero alternative should be ignored, did algo was changed?"
        );
    }


    @ParameterizedTest
    @ValueSource(strings = {
            // trailng zeros
            "_",
            "___",
            "___ ",

            // other characters
            "$",
            "%",
            "&",
            "?",
            "aaaa?aaaa",
            " ",
            "  ",

            // other characters
            "",
    })
    @NullAndEmptySource
    @DisplayName("Test invalid code processing")
    void invalidCodeTest(final String code) {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> NumericToTextConverter.generateIdFromShortLink(code)
        );
    }

    @ParameterizedTest
    @ValueSource(longs = {
            Long.MIN_VALUE,
            Long.MIN_VALUE + 1L,
            -100L,
            -1L,
            0L,
    })
    @NullSource
    @DisplayName("Test invalid ID processing")
    void invalidIDTest(final Long id) {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> NumericToTextConverter.generateShortLink(id)
        );
    }

}
