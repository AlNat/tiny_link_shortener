package dev.alnat.tinylinkshortener.service.impl;

import dev.alnat.tinylinkshortener.metric.MetricCollector;
import dev.alnat.tinylinkshortener.metric.MetricsNames;
import dev.alnat.tinylinkshortener.metric.TagNames;
import dev.alnat.tinylinkshortener.model.enums.LinkStatus;
import dev.alnat.tinylinkshortener.model.enums.VisitStatus;
import dev.alnat.tinylinkshortener.repository.LinkRepository;
import dev.alnat.tinylinkshortener.service.RedirectService;
import dev.alnat.tinylinkshortener.service.VisitService;
import dev.alnat.tinylinkshortener.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Created by @author AlNat on 14.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RedirectServiceImpl implements RedirectService {

    private final LinkRepository linkRepository;
    private final VisitService visitService;
    private final MetricCollector metricCollector;

    @Override
    public Optional<String> redirect(final String shortLink,
                                     final String ip, final String userAgent, final HttpHeaders headers) {
        var start = LocalDateTime.now();

        var linkOpt = linkRepository.searchByShortLinkWithLock(shortLink);
        if (linkOpt.isEmpty()) {
            metricCollector.pushDuration(MetricsNames.SEARCH_TIMING, Duration.between(start, LocalDateTime.now()));
            metricCollector.inc(MetricsNames.SEARCH_RESULT, TagNames.RESULT_STATUS.of(VisitStatus.NOT_FOUND.name()));

            // TODO Save visit for not existing link!
            return Optional.empty();
        }

        var link = linkOpt.get();

        VisitStatus status = VisitStatus.SUCCESSFUL;

        if (link.isNotAvailable()) {
            status = VisitStatus.NOT_AVAILABLE;
        }

        if (link.isExpired()) {
            status = VisitStatus.EXPIRED;
        }

        if (link.isTooMuchRequest()) {
            status = VisitStatus.TOO_MUCH_REQUEST;
        }

        if (link.getStatus().equals(LinkStatus.DELETED)) {
            status = VisitStatus.DELETED;
        }

        String clearedIP;
        try {
            clearedIP = Utils.normalizeIP(ip);
        } catch (Exception e) {
            log.error("Exception at normalizing IP {}, will used default", ip);
            clearedIP = "192.168.0.1";
        }

        visitService.saveNewVisit(linkOpt.get(), status, clearedIP, userAgent, headers);

        metricCollector.pushDuration(MetricsNames.SEARCH_TIMING, Duration.between(start, LocalDateTime.now()));
        metricCollector.inc(MetricsNames.SEARCH_RESULT, TagNames.RESULT_STATUS.of(status.name()));

        if (status.isRedirect()) {
            return Optional.of(link.getOriginalLink());
        }

        return Optional.empty();
    }

}
