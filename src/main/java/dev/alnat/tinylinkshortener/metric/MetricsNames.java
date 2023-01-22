package dev.alnat.tinylinkshortener.metric;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by @author AlNat on 22.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@Getter
@RequiredArgsConstructor
public enum MetricsNames {

    NEW_LINK_CREATION("link_created"),
    SEARCH_TIMING("link_search_timing"),
    SEARCH_RESULT("link_visit"),
    HANDLED_ERROR("handled_error")
    ;

    private final String metricName;

}
