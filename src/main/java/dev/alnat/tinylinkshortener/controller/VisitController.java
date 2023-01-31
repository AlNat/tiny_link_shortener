package dev.alnat.tinylinkshortener.controller;

import brave.Tracer;
import dev.alnat.tinylinkshortener.dto.LinkVisitPageResult;
import dev.alnat.tinylinkshortener.dto.LinkVisitSearchRequest;
import dev.alnat.tinylinkshortener.dto.LinkVisitStatistic;
import dev.alnat.tinylinkshortener.dto.common.Result;
import dev.alnat.tinylinkshortener.dto.common.ResultFactory;
import dev.alnat.tinylinkshortener.service.VisitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.validation.Valid;
import java.time.Duration;

/**
 * Controller for show visit to link
 * <p>
 * Use long-polling request due
 * - efficient searching (http thread parking)
 * - timeout control (if request will be too long)
 * <p>
 * Created by @author AlNat on 10.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@Validated
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/visit", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "REST API for analysis visits for links")
public class VisitController {

    private final VisitService service;
    private final Tracer tracer;

    @Value("${custom.paging.default-timeout}")
    private Duration pagingTimeout;

    // TODO RoleUser, RoleAdmin
    @Operation(summary = "Paging visit results for short link")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request completed, see result in code field in response")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("")
    public DeferredResult<LinkVisitPageResult> searchRawStatistics(
            @Parameter(in = ParameterIn.QUERY, required = true, description = "Filter to search")
            @ParameterObject @Valid final LinkVisitSearchRequest request) {
        var deferredResult = new DeferredResult<LinkVisitPageResult>(pagingTimeout.toMillis());

        deferredResult.onTimeout(() -> deferredResult.setErrorResult(ResultFactory.timeout()));

        deferredResult.onError(throwable -> {
            log.error("Exception at deferred search of paging result to {}", request, throwable);

            final String traceId = tracer.currentSpan().context().traceIdString();
            deferredResult.setErrorResult(ResultFactory.error("Something went wrong, traceId = " + traceId));
        });

        service.searchRawStatisticsAsync(request, deferredResult);

        return deferredResult;
    }

    // TODO RoleUser
    @Operation(summary = "Aggregating visit results for short link")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request completed, see result in code field in response")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/statistics/{shortLink}")
    public DeferredResult<Result<LinkVisitStatistic>> searchAggregateResult(
            @PathVariable @Parameter(in = ParameterIn.PATH, description = "Shortlink")
            final String shortLink) {
        var deferredResult = new DeferredResult<Result<LinkVisitStatistic>>(pagingTimeout.toMillis());

        deferredResult.onTimeout(() -> deferredResult.setErrorResult(ResultFactory.timeout()));

        deferredResult.onError(throwable -> {
            log.error("Exception at deferred search of statistics to {}", shortLink, throwable);

            final String traceId = tracer.currentSpan().context().traceIdString();
            deferredResult.setErrorResult(ResultFactory.error("Something went wrong, traceId = " + traceId));
        });

        service.searchAggregateStatisticsAsync(shortLink, deferredResult);

        return deferredResult;
    }

}
