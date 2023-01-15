package dev.alnat.tinylinkshortener.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * Created by @author AlNat on 11.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@Getter
@AllArgsConstructor
public enum LinkStatus {
    CREATED(0, "Link created"),
    DELETED(1, "Link deleted");

    private final int value;
    private final String message;

    public static LinkStatus ofValue(int value){
        return Arrays.stream(LinkStatus.values())
                .filter(s -> s.getValue() == value)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unknown status code!"));
    }

}
