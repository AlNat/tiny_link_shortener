package dev.alnat.tinylinkshortener.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.file.Files;

/**
 * Created by @author AlNat on 25.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@UtilityClass
public class TestUtil {

    @SneakyThrows
    public static byte[] readFromResourceFile(String fileName) {
        File file = ResourceUtils.getFile("classpath:" + fileName);
        return Files.readAllBytes(file.toPath());
    }

}
