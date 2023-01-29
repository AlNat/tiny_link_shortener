package dev.alnat.tinylinkshortener.model.converter;

import dev.alnat.tinylinkshortener.model.enums.UserRole;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Created by @author AlNat on 28.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@Converter
public class UserRoleConverter implements AttributeConverter<UserRole, Integer> {

    @Override
    public Integer convertToDatabaseColumn(UserRole visitStatus) {
        return visitStatus == null ? null : visitStatus.getValue();
    }

    @Override
    public UserRole convertToEntityAttribute(Integer status) {
        return status == null ? null : UserRole.ofValue(status);
    }


}
