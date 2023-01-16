package dev.alnat.tinylinkshortener.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * Created by @author AlNat on 10.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Result holder for page DTO")
public abstract class PaginalResult<DTO, Request> extends Result<List<DTO>> implements Serializable {

    @Schema(description = "Request info")
    private Request request;

    @Schema(description = "Is has next page with information on request")
    private boolean hasNextPage;

}
