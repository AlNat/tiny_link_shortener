package dev.alnat.tinylinkshortener.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

/**
 * Created by @author AlNat on 14.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseLinkDTO {

    @NotNull
    @Schema(description = "Link to redirect", example = "https://google.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String originalLink;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "When link should be starts enabled", example = "2023-01-01 12:00:00",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime availableFrom;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "When link should be ends enabled", example = "2023-01-01 13:00:00",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime availableTo;

    @Positive(message = "Max visit count should be > 0!")
    @Schema(description = "Max visit for link, if it should be unlimited -- not pass it",
            example = "100", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer maxVisitCount;

}
