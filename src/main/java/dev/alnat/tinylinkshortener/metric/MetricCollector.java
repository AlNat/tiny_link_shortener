package dev.alnat.tinylinkshortener.metric;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;

/**
 * Created by @author AlNat on 22.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@SuppressWarnings("unused")
public interface MetricCollector {

    void push(final MetricsNames metricsName,
              final long value);

    default void push(final MetricsNames metricsName,
              final String tagName,
              final String tagValue,
              final long value) {
        push(metricsName, Collections.singletonMap(tagName, tagValue), value);
    }

    void push(final MetricsNames metricsName,
              final Map<String, String> tags,
              final long value);


    default void inc(final MetricsNames metricsName) {
        push(metricsName, 1L);
    }


    default void inc(final MetricsNames metricsName,
                     final Map<String, String> tags) {
        push(metricsName, tags, 1L);
    }

    // Duration

    void pushDuration(final MetricsNames metricsName,
                      final Duration duration);

    default void pushDuration(final MetricsNames metricsName,
                              final String tagName,
                              final String tagValue,
                              final Duration duration) {
        pushDuration(metricsName, Collections.singletonMap(tagName, tagValue), duration);
    }

    void pushDuration(final MetricsNames metricsName,
                      final Map<String, String> tags,
                      final Duration duration);

}
