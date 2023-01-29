package dev.alnat.tinylinkshortener.mapper;

import dev.alnat.tinylinkshortener.dto.UserOutDTO;
import dev.alnat.tinylinkshortener.mapper.common.EntityMapper;
import dev.alnat.tinylinkshortener.model.User;
import org.mapstruct.Mapper;

/**
 * Created by @author AlNat on 28.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<User, UserOutDTO> {
}
