package dev.alnat.tinylinkshortener.model.converter;

import dev.alnat.tinylinkshortener.model.enums.VisitStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Created by @author AlNat on 11.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@Converter
public class VisitStatusConverter implements AttributeConverter<VisitStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(VisitStatus visitStatus) {
        return visitStatus == null ? null : visitStatus.getValue();
    }

    @Override
    public VisitStatus convertToEntityAttribute(Integer status) {
        return status == null ? null : VisitStatus.ofValue(status);
    }
}
