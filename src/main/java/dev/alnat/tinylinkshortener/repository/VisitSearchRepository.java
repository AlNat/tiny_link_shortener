package dev.alnat.tinylinkshortener.repository;

import dev.alnat.tinylinkshortener.dto.LinkVisitSearchRequest;
import dev.alnat.tinylinkshortener.model.Visit;

import java.util.List;

/**
 * Created by @author AlNat on 11.01.2023.
 * Licensed by Apache License, Version 2.0
 */
public interface VisitSearchRepository {

    List<Visit> search(LinkVisitSearchRequest searchRequest);

}
