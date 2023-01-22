package dev.alnat.tinylinkshortener.metric;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.Map;

/**
 * Created by @author AlNat on 22.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@Getter
@RequiredArgsConstructor
public enum TagNames {
    RESULT_STATUS("result_status"),
    ERROR_CODE("code")
    ;


    private final String tagName;

    public Map<String, String> of(final String value) {
        return Collections.singletonMap(this.getTagName(), value);
    }


}
