package dev.alnat.tinylinkshortener.config;

import com.google.zxing.qrcode.QRCodeWriter;
import lombok.Builder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;

import java.awt.image.BufferedImage;

/**
 * Created by @author AlNat on 24.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "custom.qr")
public class QRConfigurer {

    private Integer width = 200;
    private Integer height = 200;
    private String url;


    @Bean
    public QRConfiguration qrConfiguration() {
        return QRConfiguration.builder()
                .url(url)
                .width(width)
                .height(height)
                .build();
    }

    @Bean
    public HttpMessageConverter<BufferedImage> qrImageHttpMessageConverter() {
        return new BufferedImageHttpMessageConverter();
    }

    @Bean
    public QRCodeWriter qrCodeWriter() {
        return new QRCodeWriter();
    }

    @Data
    @Builder
    public static class QRConfiguration {

        private Integer width;
        private Integer height;

        private String url;


        /**
         * Resolving of full endpoint to generate shortlink
         * For example, configured URL link `http://some-site.com/s/%s`
         * then we are adding link for full endpoint
         */
        public String resolveEndpoint(String shortlink) {
            return String.format(url, shortlink);
        }

    }

}
