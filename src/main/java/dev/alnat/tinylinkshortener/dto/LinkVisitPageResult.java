package dev.alnat.tinylinkshortener.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.alnat.tinylinkshortener.dto.common.PaginalResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by @author AlNat on 16.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Result for search of visits")
public class LinkVisitPageResult extends PaginalResult<VisitOutDTO, LinkVisitSearchRequest> implements Serializable {

    public LinkVisitPageResult(LinkVisitSearchRequest request) {
        this.setCode(200);
        this.setRequest(request);
    }

}
