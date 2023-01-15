package dev.alnat.tinylinkshortener.engine;

import java.util.Optional;

/**
 * Created by @author AlNat on 10.01.2023.
 * Licensed by Apache License, Version 2.0
 */
public interface ShortLinkGeneratorResolver {

    Optional<ShortLinkGenerator> resolve();

}
