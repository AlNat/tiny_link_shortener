package dev.alnat.tinylinkshortener.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * Created by @author AlNat on 28.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@Getter
@RequiredArgsConstructor
public enum UserRole {

    USER(0, "ROLE_USER"),
    ADMIN(1, "ROLE_ADMIN");

    private final int value;
    private final String role;

    public static UserRole ofValue(int value){
        return Arrays.stream(UserRole.values())
                .filter(s -> s.getValue() == value)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unknown role code!"));
    }

}
