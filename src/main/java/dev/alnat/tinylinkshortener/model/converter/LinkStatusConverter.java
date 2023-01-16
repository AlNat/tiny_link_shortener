package dev.alnat.tinylinkshortener.model.converter;

import dev.alnat.tinylinkshortener.model.enums.LinkStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Created by @author AlNat on 11.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@Converter
public class LinkStatusConverter implements AttributeConverter<LinkStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(LinkStatus linkStatus) {
        return linkStatus == null ? null : linkStatus.getValue();
    }

    @Override
    public LinkStatus convertToEntityAttribute(Integer status) {
        return status == null ? null : LinkStatus.ofValue(status);
    }
}
