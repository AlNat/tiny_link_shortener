package dev.alnat.tinylinkshortener.service;

import dev.alnat.tinylinkshortener.dto.UserOutDTO;
import dev.alnat.tinylinkshortener.model.enums.UserRole;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * Created by @author AlNat on 30.01.2023.
 * Licensed by Apache License, Version 2.0
 */
public interface SecurityContextRetriever {

    default Optional<UserOutDTO> getAuthUser() {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserOutDTO dto = (UserOutDTO) principal;
        return Optional.of(dto);
    }

    default boolean hasRight(UserRole role) {
        var user = getAuthUser();
        return user.isPresent() && user.get().getRole().equals(role);
    }

}
