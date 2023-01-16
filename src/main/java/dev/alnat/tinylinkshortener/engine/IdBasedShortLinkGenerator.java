package dev.alnat.tinylinkshortener.engine;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.Optional;

/**
 * ShortLink Generator strategy based on one global DB sequence,
 * that's convert value from base-ten numeric system to alphabetic-base numeric system
 * <p>
 * Algo:
 * Retrieve new id from DB sequence
 * Convert it from base-ten numeric system to ALPHABETIC-based system
 *  for example: id 5000 -> aKs
 * done
 * <p>
 * Created by @author AlNat on 10.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IdBasedShortLinkGenerator implements ShortLinkGenerator {

    private static final String SEQUENCE_NAME = "short_link_generator_id";
    private static final String SELECT_QUERY = "SELECT nextval('" +  SEQUENCE_NAME + "')";

    private final EntityManager entityManager;


    @Override
    public ShortLinkMode getMode() {
        return ShortLinkMode.SEQUENCE;
    }

    @Override
    public Optional<String> generateShortLink() {
        Long sequenceNewValue;

        try {
            sequenceNewValue = Long.valueOf(entityManager.createNativeQuery(SELECT_QUERY)
                    .getSingleResult().toString());
            log.trace("From {} received new value {}", SEQUENCE_NAME, sequenceNewValue);
        } catch (Exception e) {
            log.error("Exception ar retrieving new sequence value", e);
            return Optional.empty();
        }

        try {
            return Optional.of(NumericToTextConverter.generateShortLink(sequenceNewValue));
        } catch (Exception e) {
            log.error("Exception at converting {}", sequenceNewValue, e);
            return Optional.empty();
        }
    }

}
