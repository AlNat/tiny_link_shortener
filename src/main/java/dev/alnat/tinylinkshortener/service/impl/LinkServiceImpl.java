package dev.alnat.tinylinkshortener.service.impl;

import dev.alnat.tinylinkshortener.dto.LinkInDTO;
import dev.alnat.tinylinkshortener.dto.LinkOutDTO;
import dev.alnat.tinylinkshortener.dto.common.Result;
import dev.alnat.tinylinkshortener.dto.common.ResultFactory;
import dev.alnat.tinylinkshortener.engine.ShortLinkGeneratorResolver;
import dev.alnat.tinylinkshortener.mapper.LinkMapper;
import dev.alnat.tinylinkshortener.metric.MetricCollector;
import dev.alnat.tinylinkshortener.metric.MetricsNames;
import dev.alnat.tinylinkshortener.model.Link;
import dev.alnat.tinylinkshortener.model.enums.LinkStatus;
import dev.alnat.tinylinkshortener.model.enums.UserRole;
import dev.alnat.tinylinkshortener.repository.LinkRepository;
import dev.alnat.tinylinkshortener.repository.UserRepository;
import dev.alnat.tinylinkshortener.service.LinkService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by @author AlNat on 14.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class LinkServiceImpl extends BaseCRUDService<Link, Long> implements LinkService {
    private final UserRepository userRepository;
    private final LinkMapper mapper;
    private final ShortLinkGeneratorResolver linkGeneratorResolver;
    private final MetricCollector metricCollector;

    @Autowired
    public LinkServiceImpl(LinkRepository repository,
                           LinkMapper mapper,
                           ShortLinkGeneratorResolver linkGeneratorResolver,
                           MetricCollector metricCollector,
                           UserRepository userRepository) {
        super(repository);
        this.mapper = mapper;
        this.linkGeneratorResolver = linkGeneratorResolver;
        this.metricCollector = metricCollector;
        this.userRepository = userRepository;
    }


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

        // TODO if anonymous

        if(getAuthUser().isPresent()) {
            link.setUser(userRepository.getReferenceById(getAuthUser().get().getId()));
        }
        link = repository.save(link);

        log.info("Generate new link {} to {} (id will be {}", link.getShortLink(), link.getOriginalLink(), link.getId());
        metricCollector.inc(MetricsNames.NEW_LINK_CREATION);

        return ResultFactory.success(mapper.entityToDTO(link));
    }

    @Override
    public Result<LinkOutDTO> find(Long id) {
        var link = repository.findById(id);
        if (link.isEmpty()) {
            return ResultFactory.notFound();
        }
        // TODO verify auth

        return ResultFactory.success(mapper.entityToDTO(link.get()));
    }

    @Override
    public Result<LinkOutDTO> find(final String shortLink) {
        var link = ((LinkRepository) repository).findByShortLink(shortLink);
        if (link.isEmpty()) {
            return ResultFactory.notFound();
        }
        // TODO verify auth

        return ResultFactory.success(mapper.entityToDTO(link.get()));
    }

    @Override
    @Transactional
    public Result<Void> deactivate(final Long id) {
        var linkOpt = repository.findById(id);
        if (linkOpt.isEmpty()) {
            return ResultFactory.notFound();
        }

        return deactivate(linkOpt.get());
    }

    @Override
    @Transactional
    public Result<Void> deactivate(final String shortLink) {
        var linkOpt = ((LinkRepository) repository).findByShortLink(shortLink);
        if (linkOpt.isEmpty()) {
            return ResultFactory.notFound();
        }

        return deactivate(linkOpt.get());
    }

    private Result<Void> deactivate(Link link) {
        Hibernate.initialize(link.getUser());

        // Only admin can delete anonymous links
        if (link.getUser() == null && !hasRight(UserRole.ADMIN)) {
            throw new InsufficientAuthenticationException("1");
        } else if (getAuthUser().isPresent() &&
                !link.getUser().getId().equals(getAuthUser().get().getId())) {
            throw new InsufficientAuthenticationException("2");
        }

        link.setStatus(LinkStatus.DELETED);
        repository.save(link);

        return ResultFactory.success();
    }

}
