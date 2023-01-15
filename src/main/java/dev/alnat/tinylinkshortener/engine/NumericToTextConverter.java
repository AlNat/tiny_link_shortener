package dev.alnat.tinylinkshortener.engine;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

/**
 * Convertor from 10-based to ALPHABETIC-based system
 * Just like convertor from 2-based to 10 based and reversal, but instead of 2 its ALPHABET
 * <p>
 * ALPHABET includes all symbols from A-z and 0-9, plus specials - _
 * but excluding some can be misunderstanding:
 *  I, i, L, l, 1 and O, o, 0
 * <p>
 * WARNING
 * _ is like 0 in ten-based -- leading zero is not should be
 * so __a is absolute equivalent of a
 * just like 01 is equal to 1
 * <p>
 * Created by @author AlNat on 10.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NumericToTextConverter {

    private static final String ALPHABET = "_abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789-";
    private static final char[] ALPHABET_ARRAY = ALPHABET.toCharArray();
    private static final int BASE = ALPHABET.length(); // 56 FYI
    private static final char ZERO_SYMBOL = ALPHABET_ARRAY[0]; // _ FYI


    public static String generateShortLink(final Long externalId) {
        Assert.notNull(externalId, "ID should be not null!");
        Assert.isTrue(externalId > 0, "ID should be positive!");

        long id = externalId;

        // convert id to text digit by digit
        StringBuilder str = new StringBuilder();
        while (id > 0) {
            int position = (int) (id % BASE);
            str.append(ALPHABET_ARRAY[position]);
            id /= BASE;
        }

        return str.reverse().toString();
    }

    public static Long generateIdFromShortLink(final String externalCode) {
        Assert.hasText(externalCode, "ID should be not empty!");

        var formatted = externalCode.strip().intern();
        var code = cutLeadingZero(formatted);

        long link = 0;
        for (int t = 0; t < code.length(); t++) { // Going by digits
            char c = code.charAt(t);
            int charPosition = -1;

            for (int i = 0; i < ALPHABET_ARRAY.length; i++) {
                if (ALPHABET_ARRAY[i] == c) {
                    charPosition = i;
                    break;
                }
            }

            // if symbol is unacceptable
            if (charPosition == -1) {
                throw new IllegalArgumentException(c + "is not valid symbol in short code!");
            }

            link = link * BASE + charPosition;
        }

        return link;
    }

    private static String cutLeadingZero(String code) {
        if (code.charAt(0) != ZERO_SYMBOL) {
            return code;
        }

        char[] codeChars = code.toCharArray();
        int prefixEnd = -1;

        for (int i = 0; i < codeChars.length; i++) {
            if (codeChars[i] == ZERO_SYMBOL) {
                continue;
            }

            // found first not zero character
            prefixEnd = i;
            break;
        }

        // 00000 situation, for example
        if (prefixEnd == -1) {
            throw new IllegalArgumentException("Code contains only leading zero characters!");
        }

        return code.substring(prefixEnd);
    }

}
