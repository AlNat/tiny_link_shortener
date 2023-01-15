package dev.alnat.tinylinkshortener.dto.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

/**
 * Created by @author AlNat on 13.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@SuppressWarnings("rawtypes")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResultFactory {

    public static <DTO> Result<DTO> success(DTO dto) {
        return Result.success(HttpStatus.OK.value(), dto);
    }

    public static <T>Result<T> success() {
        return Result.success(HttpStatus.OK.value(), null);
    }

    public static <T>Result<T> timeout() {
        return Result.error(HttpStatus.REQUEST_TIMEOUT.value(), "Request processing is too long", null);
    }

    public static <T>Result<T> badRequest(final String error) {
        return Result.error(HttpStatus.BAD_REQUEST.value(), error, null);
    }

    public static <T>Result<T> badRequest(final Map<String, List<String>> errors) {
        return Result.badRequest(HttpStatus.BAD_REQUEST.value(), errors);
    }

    public static <T>Result<T> notFound() {
        return Result.error(HttpStatus.NOT_FOUND.value());
    }

    public static <T>Result<T> error(final String error) {
        return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), error, null);
    }

}
