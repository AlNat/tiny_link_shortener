package dev.alnat.tinylinkshortener.metric;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by @author AlNat on 22.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Primary
public class MicrometerMetricCollector implements MetricCollector {

    @Value("${custom.metrics.metric-prefix:}")
    private String metricPrefix;

    private final MeterRegistry meterRegistry;


    @Override
    public synchronized void push(final MetricsNames metricsName,
                                  final long value) {
        meterRegistry
                .counter(metricPrefix + metricsName.getMetricName())
                .increment(value);
    }

    @Override
    public synchronized void push(final MetricsNames metricsName,
                                  final String tagName,
                                  final String tagValue,
                                  final long value) {
        meterRegistry
                .counter(metricPrefix + metricsName.getMetricName(), Tags.of(tagName, tagValue))
                .increment(value);
    }

    @Override
    public synchronized void push(MetricsNames metricsName, Map<String, String> tags, long value) {
        var microTags = tags.entrySet()
                .stream()
                .map(e -> Tag.of(e.getKey(), e.getValue()))
                .collect(Collectors.toList());

        meterRegistry
                .counter(metricPrefix + metricsName.getMetricName(), Tags.of(microTags))
                .increment(value);
    }

    // Duration

    @Override
    public void pushDuration(MetricsNames metricsName, Duration duration) {
        meterRegistry
                .timer(metricPrefix + metricsName.getMetricName())
                .record(duration);
    }

    @Override
    public void pushDuration(MetricsNames metricsName, Map<String, String> tags, Duration duration) {
        var microTags = tags
                .entrySet()
                .stream()
                .map(e -> Tag.of(e.getKey(), e.getValue()))
                .collect(Collectors.toList());

        meterRegistry
                .timer(metricPrefix + metricsName.getMetricName(), Tags.of(microTags))
                .record(duration);
    }

}
