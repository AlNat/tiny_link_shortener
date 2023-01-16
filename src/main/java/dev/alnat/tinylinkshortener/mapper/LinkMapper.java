package dev.alnat.tinylinkshortener.mapper;

import dev.alnat.tinylinkshortener.dto.LinkInDTO;
import dev.alnat.tinylinkshortener.dto.LinkOutDTO;
import dev.alnat.tinylinkshortener.mapper.common.EntityMapper;
import dev.alnat.tinylinkshortener.model.Link;
import org.mapstruct.Mapper;

/**
 * Created by @author AlNat on 13.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@Mapper(componentModel = "spring")
public interface LinkMapper extends EntityMapper<Link, LinkOutDTO> {

    Link inDTO(LinkInDTO dto);

}
