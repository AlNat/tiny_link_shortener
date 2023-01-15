package dev.alnat.tinylinkshortener.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by @author AlNat on 10.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Result holder for some DTO")
public class Result<DTO> implements Serializable {

    @Schema(description = "Result code", requiredMode = Schema.RequiredMode.REQUIRED)
    private int code;

    @Schema(description = "DTO of result itself")
    private DTO data;

    @Schema(description = "Description of code if necessary")
    private String description;

    @Schema(description = "Details of validation errors")
    private Map<String, List<String>> fieldsErrors;



    public static <DTO> Result<DTO> success(final DTO data) {
        return success(200, data);
    }

    public static <DTO> Result<DTO> success(int code) {
        return new Result<>(code, null, null, null);
    }

    public static <DTO> Result<DTO> success(int code, final DTO data) {
        return new Result<>(code, data, null, null);
    }

    public static <DTO> Result<DTO> badRequest(int code, Map<String, List<String>> fieldsErrors) {
        return new Result<>(code, null, null, fieldsErrors);
    }

    public static <DTO> Result<DTO> error(int code) {
        return new Result<>(code, null, null, null);
    }

    public static <DTO> Result<DTO> error(int code, String message) {
        return new Result<>(code, null, message, null);
    }

    public static <DTO> Result<DTO> error(int code, String message, final DTO data) {
        return new Result<>(code, data, message, null);
    }

}
