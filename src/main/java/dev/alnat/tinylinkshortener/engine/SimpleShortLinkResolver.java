package dev.alnat.tinylinkshortener.engine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by @author AlNat on 10.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@Slf4j
@Service
public class SimpleShortLinkResolver implements ShortLinkGeneratorResolver {

    @Value("${custom.short-link-generator-mode}")
    private ShortLinkMode defaultMode;

    private final Map<ShortLinkMode, ShortLinkGenerator> generatorMap;

    @Autowired
    public SimpleShortLinkResolver(List<ShortLinkGenerator> generatorList) {
        this.generatorMap = generatorList
                .stream()
                .collect(Collectors.toMap(
                        ShortLinkGenerator::getMode, Function.identity(),
                        (engine1, engine2) -> {
                            log.error("There is 2 generators with same mode! Engines are {} and {}, mode {}",
                                    engine1.getClass().getSimpleName(), engine2.getClass().getSimpleName(), engine2.getMode());
                            throw new IllegalStateException("FDuplicate generators for one mode!");
                        }
                ));
    }

    @Override
    public Optional<ShortLinkGenerator> resolve() {
        return Optional.ofNullable(generatorMap.get(defaultMode));
    }

}
