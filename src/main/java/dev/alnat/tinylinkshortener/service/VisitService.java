package dev.alnat.tinylinkshortener.service;

import dev.alnat.tinylinkshortener.dto.LinkVisitPageResult;
import dev.alnat.tinylinkshortener.dto.LinkVisitSearchRequest;
import dev.alnat.tinylinkshortener.dto.LinkVisitStatistic;
import dev.alnat.tinylinkshortener.dto.VisitOutDTO;
import dev.alnat.tinylinkshortener.dto.common.PaginalResult;
import dev.alnat.tinylinkshortener.dto.common.Result;
import dev.alnat.tinylinkshortener.model.Link;
import dev.alnat.tinylinkshortener.model.Visit;
import dev.alnat.tinylinkshortener.model.enums.VisitStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.Optional;

/**
 * Created by @author AlNat on 12.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@SuppressWarnings("unused")
public interface VisitService extends BaseService<Visit, Long> {

    Long saveNewVisit(final Link link, final VisitStatus status,
                      final String ip, final String userAgent, final HttpHeaders headers);


    Optional<VisitOutDTO> findById(Long id);

    List<VisitOutDTO> findByParams(LinkVisitSearchRequest request);


    LinkVisitPageResult searchRawStatistics(final LinkVisitSearchRequest request);

    void searchRawStatisticsAsync(final LinkVisitSearchRequest request,
                                  DeferredResult<LinkVisitPageResult> result);

    Result<LinkVisitStatistic> searchAggregateStatistics(final String shortLink);

    void searchAggregateStatisticsAsync(final String shortLink,
                                        DeferredResult<Result<LinkVisitStatistic>> result);

}
