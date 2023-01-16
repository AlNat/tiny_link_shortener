package dev.alnat.tinylinkshortener.service.impl;

import dev.alnat.tinylinkshortener.dto.LinkInDTO;
import dev.alnat.tinylinkshortener.dto.LinkOutDTO;
import dev.alnat.tinylinkshortener.dto.common.Result;
import dev.alnat.tinylinkshortener.dto.common.ResultFactory;
import dev.alnat.tinylinkshortener.engine.ShortLinkGeneratorResolver;
import dev.alnat.tinylinkshortener.mapper.LinkMapper;
import dev.alnat.tinylinkshortener.model.enums.LinkStatus;
import dev.alnat.tinylinkshortener.repository.LinkRepository;
import dev.alnat.tinylinkshortener.service.LinkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by @author AlNat on 14.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LinkServiceImpl implements LinkService {

    private final LinkRepository repository;
    private final LinkMapper mapper;
    private final ShortLinkGeneratorResolver linkGeneratorResolver;


    @Override
    @Transactional
    public Result<LinkOutDTO> create(final LinkInDTO dto) {
        var generator = linkGeneratorResolver.resolve();
        if(generator.isEmpty()) {
            log.error("Not found link generator!");
            return ResultFactory.error("Link generator error");
        }

        var linkOpt = generator.get().generateShortLink();
        if (linkOpt.isEmpty()) {
            log.error("Link generator {} cant create shortlink!", generator.get().getClass().getSimpleName());
            return ResultFactory.error("Shortlink generation error");
        }

        var shortLink = linkOpt.get();

        var link = mapper.inDTO(dto);
        link.setShortLink(shortLink);
        link.setStatus(LinkStatus.CREATED);

        link = repository.save(link);

        log.info("Generate new link {} to {} (id will be {}", link.getShortLink(), link.getOriginalLink(), link.getId());

        return ResultFactory.success(mapper.entityToDTO(link));
    }

    @Override
    public Result<LinkOutDTO> find(Long id) {
        var link = repository.findById(id);
        if (link.isEmpty()) {
            return ResultFactory.notFound();
        }

        return ResultFactory.success(mapper.entityToDTO(link.get()));
    }

    @Override
    public Result<LinkOutDTO> find(final String shortLink) {
        var link = repository.findByShortLink(shortLink);
        if (link.isEmpty()) {
            return ResultFactory.notFound();
        }

        return ResultFactory.success(mapper.entityToDTO(link.get()));
    }

    @Override
    @Transactional
    public Result<Void> deactivate(final Long id) {
        var linkOpt = repository.findById(id);
        if (linkOpt.isEmpty()) {
            return ResultFactory.notFound();
        }

        var link = linkOpt.get();
        link.setStatus(LinkStatus.DELETED);
        repository.save(link);

        return ResultFactory.success();
    }

    @Override
    @Transactional
    public Result<Void> deactivate(final String shortLink) {
        var linkOpt = repository.findByShortLink(shortLink);
        if (linkOpt.isEmpty()) {
            return ResultFactory.notFound();
        }

        var link = linkOpt.get();
        link.setStatus(LinkStatus.DELETED);
        repository.save(link);

        return ResultFactory.success();
    }

}
