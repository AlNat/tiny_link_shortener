package dev.alnat.tinylinkshortener.controller.handler;

import brave.Tracer;
import dev.alnat.tinylinkshortener.dto.common.Result;
import dev.alnat.tinylinkshortener.dto.common.ResultFactory;
import dev.alnat.tinylinkshortener.metric.MetricCollector;
import dev.alnat.tinylinkshortener.metric.MetricsNames;
import dev.alnat.tinylinkshortener.metric.TagNames;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Created by @author AlNat on 10.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final Tracer tracer;
    private final MetricCollector metricCollector;


    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                                     HttpHeaders headers,
                                                                     HttpStatus status,
                                                                     WebRequest request) {
        metricCollector.inc(MetricsNames.HANDLED_ERROR, TagNames.ERROR_CODE.of("415"));

        return new ResponseEntity<>(
                Result.error(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), HttpStatus.UNSUPPORTED_MEDIA_TYPE.getReasonPhrase()),
                HttpStatus.OK
        );
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        var errors = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(e -> e.getPropertyPath().toString(),
                        e -> Collections.singletonList(e.getMessage()),
                        (e1, e2) -> {
                            var list = new ArrayList<String>(e1.size() + e2.size());
                            list.addAll(e1);
                            list.addAll(e2);
                            return list;
                        }));

        log.warn("Validation error: {}", errors, ex);

        metricCollector.inc(MetricsNames.HANDLED_ERROR, TagNames.ERROR_CODE.of("400"));

        return new ResponseEntity<>(ResultFactory.badRequest(errors), HttpStatus.OK);
    }


    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        metricCollector.inc(MetricsNames.HANDLED_ERROR, TagNames.ERROR_CODE.of("400"));
        return new ResponseEntity<>(
                ResultFactory.badRequest(ex.getMessage()),
                HttpStatus.OK
        );
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        var errors = ex.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField,
                        e -> Collections.singletonList(e.getDefaultMessage()),
                        (e1, e2) -> {
                            var list = new ArrayList<String>(e1.size() + e2.size());
                            list.addAll(e1);
                            list.addAll(e2);
                            return list;
                        }));

        log.warn("Validation error: {}", errors, ex);

        metricCollector.inc(MetricsNames.HANDLED_ERROR, TagNames.ERROR_CODE.of("400"));

        return new ResponseEntity<>(ResultFactory.badRequest(errors), HttpStatus.OK);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex,
                                                             @Nullable Object body,
                                                             HttpHeaders headers,
                                                             HttpStatus status,
                                                             WebRequest request) {
        log.error("Handle unexpected exception: {}", ex.getMessage(), ex);
        metricCollector.inc(MetricsNames.HANDLED_ERROR, TagNames.ERROR_CODE.of("500"));

        final String traceId = tracer.currentSpan().context().traceIdString();
        return ResponseEntity
                .internalServerError()
                .body(ResultFactory.error("Internal Server Error, traceId=[" + traceId + "]"));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex) {
        log.warn("Security exception", ex);
        return new ResponseEntity<>(
                ResultFactory.unauthorized(),
                HttpStatus.OK
        );
    }

    /**
     * Global exception
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(final Exception ex) {
        log.error("Handle unexpected exception: {}", ex.getMessage(), ex);
        metricCollector.inc(MetricsNames.HANDLED_ERROR, TagNames.ERROR_CODE.of("500"));

        final String traceId = tracer.currentSpan().context().traceIdString();
        return ResponseEntity
                .internalServerError()
                .body(ResultFactory.error("Internal Server Error, traceId=[" + traceId + "]"));
    }

}
