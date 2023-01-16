package dev.alnat.tinylinkshortener.engine;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by @author AlNat on 15.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@Getter
@RequiredArgsConstructor
public enum ShortLinkMode {
    SEQUENCE("Generate short link on DB sequence");

    private final String description;

}
