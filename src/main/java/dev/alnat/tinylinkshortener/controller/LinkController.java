package dev.alnat.tinylinkshortener.controller;

import dev.alnat.tinylinkshortener.dto.LinkInDTO;
import dev.alnat.tinylinkshortener.dto.LinkOutDTO;
import dev.alnat.tinylinkshortener.dto.common.Result;
import dev.alnat.tinylinkshortener.service.LinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

/**
 * Created by @author AlNat on 10.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@Validated
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/link", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "REST API for operation links")
public class LinkController {

    private final LinkService linkService;

    @Operation(summary = "Create new shortlink")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request completed, see result in code field in response")
    })
    @PostMapping(value =  "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Result<LinkOutDTO> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO for new short link request", required = true)
            @RequestBody @Valid final LinkInDTO dto) {
        return linkService.create(dto);
    }

    @Operation(summary = "Search shortlink by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request completed, see result in code field in response")
    })
    @GetMapping("/{id}")
    public Result<LinkOutDTO> find(@Parameter(in = ParameterIn.PATH, description = "Link ID", required = true)
                                   @PathVariable @Positive Long id) {
        return linkService.find(id);
    }

    @Operation(summary = "Search shortlink by shortlink (as lookup method)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request completed, see result in code field in response")
    })
    @GetMapping(value = "", params = "shortLink")
    public Result<LinkOutDTO> find(@Parameter(in = ParameterIn.QUERY, description = "Shortlink", required = true)
                                   @RequestParam @NotEmpty String shortLink) {
        return linkService.find(shortLink);
    }

    @Operation(summary = "Deactivate shortlink by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request completed, see result in code field in response")
    })
    @DeleteMapping("/{id}")
    public Result<Void> deactivate(@Parameter(in = ParameterIn.PATH, description = "Link ID", required = true)
                                   @PathVariable @Positive Long id) {
        return linkService.deactivate(id);
    }

    @Operation(summary = "Deactivate shortlink by shortlink")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request completed, see result in code field in response")
    })
    @DeleteMapping(value = "", params = "shortLink")
    public Result<Void> deactivate(@Parameter(in = ParameterIn.QUERY, description = "Shortlink", required = true)
                                   @RequestParam @NotEmpty String shortLink) {
        return linkService.deactivate(shortLink);
    }

}
