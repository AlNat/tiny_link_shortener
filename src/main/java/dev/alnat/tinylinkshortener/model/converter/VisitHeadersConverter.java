package dev.alnat.tinylinkshortener.model.converter;

import org.springframework.http.HttpHeaders;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Created by @author AlNat on 11.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@Converter
public class VisitHeadersConverter implements AttributeConverter<HttpHeaders, String> {

    @Override
    public String convertToDatabaseColumn(HttpHeaders attribute) {
        // TODO Custom serialization
        return attribute.toString();
    }

    @Override
    public HttpHeaders convertToEntityAttribute(String dbData) {
        // TODO custom deserialization
        return new HttpHeaders();
    }

}
