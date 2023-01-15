package dev.alnat.tinylinkshortener.repository;

import dev.alnat.tinylinkshortener.model.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Optional;

/**
 * Created by @author AlNat on 11.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {

    // due method name more obviously
    default Optional<Link> searchByShortLinkWithLock(String shortLink) {
        return searchByShortLink(shortLink);
    }

    // Do lock to ensure that's visit counter will be correct
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints(value = {
            @QueryHint(name = "javax.persistence.lock.timeout", value = "500")
    })
    Optional<Link> searchByShortLink(String shortLink);

    Optional<Link> findByShortLink(String shortLink);

}
