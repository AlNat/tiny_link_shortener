package dev.alnat.tinylinkshortener.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Created by @author AlNat on 26.02.2023.
 * Licensed by Apache License, Version 2.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestConstants {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Link {
        public static final String REDIRECT_TO = "https://google.com/q=test";
    }

}
