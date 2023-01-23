package dev.alnat.tinylinkshortener.service.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import dev.alnat.tinylinkshortener.config.QRConfigurer.QRConfiguration;
import dev.alnat.tinylinkshortener.metric.MetricCollector;
import dev.alnat.tinylinkshortener.metric.MetricsNames;
import dev.alnat.tinylinkshortener.service.QRGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

/**
 * Created by @author AlNat on 24.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ZxingQRCodeGenerator implements QRGenerator {

    private final QRConfiguration qrConfiguration;
    private final QRCodeWriter qrCodeWriter;
    private final MetricCollector metricCollector;


    @Override
    public BufferedImage generateQRCode(final String shortLink) {
        final String endpoint = qrConfiguration.resolveEndpoint(shortLink);

        final BitMatrix bitMatrix;
        try {
            bitMatrix = qrCodeWriter.encode(endpoint, BarcodeFormat.QR_CODE,
                    qrConfiguration.getWidth(), qrConfiguration.getHeight());
        } catch (WriterException e) {
            log.error("Exception at generating QR code to {}", shortLink, e);
            throw new IllegalStateException("QR code cant be generated");
        }


        final var qr = MatrixToImageWriter.toBufferedImage(bitMatrix);

        log.info("QR code for {} generated", shortLink);
        metricCollector.inc(MetricsNames.QR_CODE_GENERATED);

        return qr;
    }

}
