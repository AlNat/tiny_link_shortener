package dev.alnat.tinylinkshortener.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import dev.alnat.tinylinkshortener.model.enums.VisitStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by @author AlNat on 11.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Information about the visit")
public class VisitOutDTO implements Serializable {

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "When link was visited", example = "2023-01-01 12:01:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime visitTime;

    @Schema(description = "Status of visit", example = "SUCCESSFUL", requiredMode = Schema.RequiredMode.REQUIRED)
    private VisitStatus status;

    @Schema(description = "Information about link", requiredMode = Schema.RequiredMode.REQUIRED)
    private LinkOutDTO link;

    @Schema(description = "Clients ip address", example = "192.168.0.1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String ip;

    @Schema(description = "Clients user agent", example = "IE6", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String userAgent;

}
