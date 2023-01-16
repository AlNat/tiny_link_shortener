package dev.alnat.tinylinkshortener.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Created by @author AlNat on 13.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Aggregate statistics for shortlink")
public class LinkVisitStatistic implements Serializable {

    @Schema(description = "Current visit count", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer visitCount;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Last visit datetime", example = "2023-01-01 13:00:00", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime lastVisitTime;

    @Schema(description = "Visits grouped by date", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Map<LocalDate, Long> visitsByDay;

    @Schema(description = "Information about link", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    private LinkOutDTO link;

}
