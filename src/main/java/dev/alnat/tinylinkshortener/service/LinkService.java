package dev.alnat.tinylinkshortener.service;

import dev.alnat.tinylinkshortener.dto.LinkInDTO;
import dev.alnat.tinylinkshortener.dto.LinkOutDTO;
import dev.alnat.tinylinkshortener.dto.common.Result;

/**
 * Created by @author AlNat on 12.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@SuppressWarnings("unused")
public interface LinkService {

    Result<LinkOutDTO> create(final LinkInDTO dto);

    Result<LinkOutDTO> find(final Long id);

    Result<LinkOutDTO> find(final String shortLink);

    // TODO FindPage

    Result<Void> deactivate(final Long id);

    Result<Void> deactivate(final String shortLink);

}
