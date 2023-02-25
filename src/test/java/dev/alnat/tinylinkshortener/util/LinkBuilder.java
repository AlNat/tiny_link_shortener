package dev.alnat.tinylinkshortener.util;

import dev.alnat.tinylinkshortener.model.Link;
import dev.alnat.tinylinkshortener.model.enums.LinkStatus;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.LocalDateTime;

import static dev.alnat.tinylinkshortener.util.TestConstants.Link.FIRST_SHORT_LINK;
import static dev.alnat.tinylinkshortener.util.TestConstants.Link.REDIRECT_TO;

/**
 * Builder for get Link
 * @see Link
 * Initialize simple default test values, can be ovveritiong with builder
 *
 * Created by @author AlNat on 26.02.2023.
 * Licensed by Apache License, Version 2.0
 */
@AllArgsConstructor
@NoArgsConstructor(staticName = "someLink")
@With
public class LinkBuilder implements Builder<Link> {

    private LocalDateTime availableFrom = null;
    private LocalDateTime availableTo = null;
    private LocalDateTime created = LocalDateTime.now();
    private String originalLink = REDIRECT_TO;
    private String shortLink = FIRST_SHORT_LINK;
    private LinkStatus status = LinkStatus.CREATED;
    private Integer maxVisitCount = null;


    private boolean switched = false;

    @Override
    public Link build() {
        final var link = new Link();
        link.setAvailableFrom(availableFrom);
        link.setAvailableTo(availableTo);
        link.setCreated(created);
        link.setOriginalLink(originalLink);
        link.setShortLink(shortLink);
        link.setStatus(status);
        link.setMaxVisitCount(maxVisitCount);
        return link;
    }

}
