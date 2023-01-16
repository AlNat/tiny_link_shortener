package dev.alnat.tinylinkshortener.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;

/**
 * Created by @author AlNat on 10.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@Data
@ToString(callSuper = true)
@RequiredArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Request with sorting and pagination")
public abstract class PaginalSortingRequest extends PaginalRequest implements Serializable {

    @Schema(description = "Sort by this filed")
    private String filedSort;

    @Schema(description = "Sorting of result", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Sorting sorting;

}
