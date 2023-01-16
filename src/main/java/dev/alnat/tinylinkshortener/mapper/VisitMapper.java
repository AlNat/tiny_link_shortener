package dev.alnat.tinylinkshortener.mapper;

import dev.alnat.tinylinkshortener.dto.VisitOutDTO;
import dev.alnat.tinylinkshortener.mapper.common.EntityMapper;
import dev.alnat.tinylinkshortener.model.Visit;
import io.hypersistence.utils.hibernate.type.basic.Inet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Created by @author AlNat on 13.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@Mapper(componentModel = "spring", uses = LinkMapper.class)
public interface VisitMapper extends EntityMapper<Visit, VisitOutDTO> {

    @Override
    @Mapping(source = "created", target = "visitTime")
    VisitOutDTO entityToDTO(Visit domain);

    default Inet map(String value) {
        return new Inet(value);
    }

    default String map(Inet inet) {
        return inet.getAddress();
    }

}
