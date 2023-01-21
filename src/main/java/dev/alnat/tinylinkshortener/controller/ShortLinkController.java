package dev.alnat.tinylinkshortener.controller;

import brave.Tracer;
import dev.alnat.tinylinkshortener.service.RedirectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Optional;

/**
 * Controller for redirects on requesting shortlink
 * <p>
 * Created by @author AlNat on 10.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@Slf4j
@RestController
@Setter
@RequestMapping(value = "/s/", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Controller for requesting shortlinks", description = "Front-end controller for users")
public class ShortLinkController {

    private final Tracer tracer;
    private final RedirectService service;

    private String htmlFromResources;

    @Value("${custom.proxy.is-behind-poxy}")
    private Boolean isProxy;

    @Value("${custom.proxy.client-ip-address-header}")
    private String ipAddressHeaderName;

    @Autowired
    public ShortLinkController(Tracer tracer,
                               RedirectService service,
                               @Value("classpath:not_found_page.html") Resource notFoundPage) {
        this.tracer = tracer;
        this.service = service;

        try {
            this.htmlFromResources = StreamUtils.copyToString(notFoundPage.getInputStream(), Charset.defaultCharset());
        } catch (IOException e) {
            log.error("Exception when reading HTML from resources!", e);
            this.htmlFromResources = "Sorry, but shortlink is not found :("; // Default error
        }
    }

    @Operation(summary = "Redirect for short link visit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Link found and redirection will be completed"),
            @ApiResponse(responseCode = "404", description = "Not found link"),
            @ApiResponse(responseCode = "500", description = "Some error")
    })
    @GetMapping("{shortlink}")
    public ResponseEntity<String> redirect(@Parameter(in = ParameterIn.PATH, required = true, description = "Shortlink")
                                            @PathVariable final String shortlink,
                                           @Parameter(hidden = true) @RequestHeader(value = "User-Agent", required = false) String userAgent,
                                           @Parameter(hidden = true) @RequestHeader HttpHeaders headers,
                                           @Parameter(hidden = true) HttpServletRequest request) {
        String clientIp;
        if (Boolean.TRUE.equals(isProxy)) {
            clientIp = headers.getFirst(ipAddressHeaderName);
        } else {
            clientIp = request.getRemoteAddr();
        }

        Optional<String> found;
        try {
            found = service.redirect(shortlink, clientIp, userAgent, headers);
        } catch (Exception e) {
            final String traceId = tracer.currentSpan().context().traceIdString();
            return ResponseEntity
                    .internalServerError()
                    .contentType(MediaType.TEXT_HTML)
                    .body("Something went wrong :(, traceId=[" + traceId + "]");
        }

        if (found.isEmpty()) {
            log.warn("Not found link for {}", shortlink);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.TEXT_HTML)
                    .body(htmlFromResources);
        }
        log.debug("By {} found {}, will redirect", shortlink, found.get());

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .header("Location", found.get())
                .build();
    }

}
