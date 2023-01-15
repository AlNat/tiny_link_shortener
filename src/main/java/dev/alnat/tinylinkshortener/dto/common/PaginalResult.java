package dev.alnat.tinylinkshortener.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

/**
 * Created by @author AlNat on 10.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Result holder for page DTO")
public class PaginalResult<DTO> extends Result<List<DTO>> {

    @Schema(description = "Request info")
    private PaginalRequest request;

    @Schema(description = "Is has next page with information on request")
    private boolean hasNextPage;

}
