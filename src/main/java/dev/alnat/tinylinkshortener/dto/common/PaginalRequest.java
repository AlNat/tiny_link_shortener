package dev.alnat.tinylinkshortener.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Created by @author AlNat on 10.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@Data
@ToString
@Schema(description = "Sort by this filed")
public abstract class PaginalRequest {

    @Schema(description = "Limit of request data", requiredMode = Schema.RequiredMode.NOT_REQUIRED, defaultValue = "100")
    @Min(1) @Max(100)
    private Integer limit = 100;

    @Schema(description = "Offset of request data", requiredMode = Schema.RequiredMode.NOT_REQUIRED, defaultValue = "0")
    @Min(0)
    private Integer offset = 0;

}
