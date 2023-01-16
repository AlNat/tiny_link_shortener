package dev.alnat.tinylinkshortener.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import dev.alnat.tinylinkshortener.model.enums.LinkStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by @author AlNat on 11.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Information about link")
public class LinkOutDTO extends BaseLinkDTO implements Serializable {

    @Schema(description = "Link ID", example = "332211")
    private Long id;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Creation date time", example = "2023-01-01 12:00:00")
    private LocalDateTime created;

    @Schema(description = "Shortlink", example = "adb")
    private String shortLink;

    @Schema(description = "Current visit time", example = "3")
    private Integer currentVisitCount;

    @Schema(description = "Link status", example = "CREATED")
    private LinkStatus status;

}
