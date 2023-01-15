package dev.alnat.tinylinkshortener.service;

import org.springframework.http.HttpHeaders;

import java.util.Optional;

/**
 * Created by @author AlNat on 14.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@SuppressWarnings("unused")
public interface RedirectService {

    Optional<String> redirect(final String shortLink,
                              final String ip, final String userAgent, final HttpHeaders headers);

}
