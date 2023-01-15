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
public enum VisitStatus {
    SUCCESSFUL(0, "Visit successful", true),
    NOT_FOUND(1, "Link not found", false),
    DELETED(2, "Link deleted", false),
    NOT_AVAILABLE(3, "Link not available yet", false),
    EXPIRED(4, "Link expired", false),
    TOO_MUCH_REQUEST(5, "Link expires all visits", false);

    private final int value;
    private final String message;
    private boolean isRedirect;

    public static VisitStatus ofValue(int value){
        return Arrays.stream(VisitStatus.values())
                .filter(s -> s.getValue() == value)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unknown status code!"));
    }

}
