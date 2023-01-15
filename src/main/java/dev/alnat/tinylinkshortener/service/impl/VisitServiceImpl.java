package dev.alnat.tinylinkshortener.service.impl;

import dev.alnat.tinylinkshortener.dto.LinkVisitSearchRequest;
import dev.alnat.tinylinkshortener.dto.LinkVisitStatistic;
import dev.alnat.tinylinkshortener.dto.VisitOutDTO;
import dev.alnat.tinylinkshortener.dto.common.PaginalResult;
import dev.alnat.tinylinkshortener.dto.common.Result;
import dev.alnat.tinylinkshortener.dto.common.ResultFactory;
import dev.alnat.tinylinkshortener.mapper.LinkMapper;
import dev.alnat.tinylinkshortener.mapper.VisitMapper;
import dev.alnat.tinylinkshortener.model.Link;
import dev.alnat.tinylinkshortener.model.Visit;
import dev.alnat.tinylinkshortener.model.enums.VisitStatus;
import dev.alnat.tinylinkshortener.repository.LinkRepository;
import dev.alnat.tinylinkshortener.repository.VisitRepository;
import dev.alnat.tinylinkshortener.service.VisitService;
import dev.alnat.tinylinkshortener.util.Utils;
import io.hypersistence.utils.hibernate.type.basic.Inet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by @author AlNat on 14.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VisitServiceImpl implements VisitService {
    private final LinkRepository linkRepository;

    private final VisitRepository repository;
    private final VisitMapper mapper;
    private final LinkMapper linkMapper;


    @Override
    @Transactional
    public Long saveNewVisit(final Link link, final VisitStatus status,
                             final String ip, final String userAgent, final HttpHeaders headers) {
        Visit visit = new Visit();
        visit.setLink(link);
        visit.setStatus(status);
        visit.setIp(new Inet(ip));
        visit.setUserAgent(userAgent);
        visit.setHeaders(headers);

        var res = repository.save(visit);

        return res.getId();
    }

    @Override
    public Optional<VisitOutDTO> findById(Long id) {
        var visit = repository.findById(id);
        if (visit.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(mapper.entityToDTO(visit.get()));
    }

    @Override
    public List<VisitOutDTO> findByParams(final LinkVisitSearchRequest request) {
        var visitList = repository.search(request);
        if (visitList.isEmpty()) {
            return Collections.emptyList();
        }

        return mapper.entityToDTO(visitList);
    }


    @Override
    public PaginalResult<VisitOutDTO> searchRawStatistics(final LinkVisitSearchRequest request) {
        var page = repository.search(request);

        PaginalResult<VisitOutDTO> result = new PaginalResult<>();
        result.setRequest(request);

        if (page.isEmpty()) {
            result.setCode(404);
            return result;
        }

        if (page.size() > request.getLimit()) {
            result.setHasNextPage(true);
            result.setData(mapper.entityToDTO(Utils.getSubList(page, request.getLimit())));
        } else {
            result.setHasNextPage(true);
            result.setData(mapper.entityToDTO(page));
        }

        return result;
    }

    @Override
    @Async
    public void searchRawStatisticsAsync(final LinkVisitSearchRequest request,
                                         DeferredResult<PaginalResult<VisitOutDTO>> result) {
        var res = searchRawStatistics(request);

        if (result.isSetOrExpired()) {
            log.warn("Search {} cant be returned", request);
            return;
        }

        result.setResult(res);
    }

    @Override
    public Result<LinkVisitStatistic> searchAggregateStatistics(final String shortLink) {
        var linkOpt = linkRepository.findByShortLink(shortLink);
        if (linkOpt.isEmpty()) {
            return ResultFactory.notFound();
        }

        var link = linkOpt.get();
        Hibernate.initialize(link.getVisitList());

        var stat = new LinkVisitStatistic();
        stat.setLink(linkMapper.entityToDTO(link));
        stat.setVisitCount(link.getCurrentVisitCount());


        if (link.getVisitList() != null && !link.getVisitList().isEmpty()) {
            var visits = link.getVisitList().stream().sorted().toList();
            stat.setLastVisitTime(visits.stream()
                    .max(Comparator.comparing(Visit::getCreated, Comparator.naturalOrder()))
                    .orElseThrow().getCreated());

            // Group by all visits per day
            stat.setVisitsByDay(visits.stream()
                    .collect(Collectors.groupingBy(v -> v.getCreated().toLocalDate(), Collectors.counting()))
            );
        }

        return ResultFactory.success(stat);
    }

    @Override
    @Async
    public void searchAggregateStatisticsAsync(final String shortLink,
                                               DeferredResult<Result<LinkVisitStatistic>> result) {
        var res = searchAggregateStatistics(shortLink);

        if (result.isSetOrExpired()) {
            log.warn("Search {} cant be returned", shortLink);
            return;
        }

        result.setResult(res);
    }

}
