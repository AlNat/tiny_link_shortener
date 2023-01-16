package dev.alnat.tinylinkshortener.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import dev.alnat.tinylinkshortener.dto.common.PaginalSortingRequest;
import dev.alnat.tinylinkshortener.model.enums.VisitStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by @author AlNat on 10.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Request for search links visit")
public class LinkVisitSearchRequest extends PaginalSortingRequest implements Serializable {

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Visits from that date", example = "2023-01-01 12:00:00",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime visitedFrom;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Visits not after that date", example = "2023-01-01 12:00:00",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime visitedTo;

    @Schema(description = "Shortlink value", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String shortLink;

    @Schema(description = "Statuses of visits", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<VisitStatus> status;

}
