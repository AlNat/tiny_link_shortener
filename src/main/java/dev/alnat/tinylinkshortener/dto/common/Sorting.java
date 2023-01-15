package dev.alnat.tinylinkshortener.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * DTO for sort directives
 *
 * Created by @author AlNat on 13.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Sorting information")
public final class Sorting {

    @Schema(description = "Sort by this field")
    private String field;

    @Schema(description = "Sort order", defaultValue = "ASC")
    private SortOrder order;

    public enum SortOrder {
        ASC, DESC;
    }

}
