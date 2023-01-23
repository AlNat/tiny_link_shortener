package dev.alnat.tinylinkshortener.controller;

import dev.alnat.tinylinkshortener.service.impl.ZxingQRCodeGenerator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import java.awt.image.BufferedImage;

/**
 * Created by @author AlNat on 24.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/qr")
@Tag(name = "REST API for generating QR codes for shortlinks")
public class QRCodeController {

    private final ZxingQRCodeGenerator qrCodeGenerator;


    @Operation(summary = "Generate QR code fo shortlink")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request completed, QR image in response")
    })
    @GetMapping(value = "/", params = "shortLink", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> generate(
            @Parameter(in = ParameterIn.QUERY, description = "Shortlink", required = true)
            @RequestParam @NotEmpty String shortLink) {
        return ResponseEntity.ok(qrCodeGenerator.generateQRCode(shortLink));
    }

}
