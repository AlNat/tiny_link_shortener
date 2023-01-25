package dev.alnat.tinylinkshortener.controller;

import dev.alnat.tinylinkshortener.dto.common.Result;
import dev.alnat.tinylinkshortener.dto.common.ResultFactory;
import dev.alnat.tinylinkshortener.service.impl.ZxingQRCodeGenerator;
import dev.alnat.tinylinkshortener.util.Utils;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.awt.image.BufferedImage;
import java.util.Base64;

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
    @GetMapping(value = "/{shortLink}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> generateQR(
            @Parameter(in = ParameterIn.PATH, description = "Shortlink", required = true)
            @PathVariable @NotEmpty String shortLink) {
        return ResponseEntity.ok(qrCodeGenerator.generateQRCode(shortLink));
    }

    @Operation(summary = "Generate QR code fo shortlink with Base64 response")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request completed, QR image in base 64 format in response")
    })
    @GetMapping(value = "/{shortLink}/base64", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result<String> generateQRWithBase64(
            @Parameter(in = ParameterIn.PATH, description = "Shortlink", required = true)
            @PathVariable @NotEmpty String shortLink) {
        try {
            var code = qrCodeGenerator.generateQRCode(shortLink);
            return ResultFactory.success(Base64.getEncoder().encodeToString(Utils.toByteArray(code)));
        } catch (Exception e) {
            log.error("Exception at generating QR to {}", shortLink, e);
            return ResultFactory.error("Unexpected error at generating QR image");
        }
    }

}
