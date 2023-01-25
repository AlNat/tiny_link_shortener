package dev.alnat.tinylinkshortener.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.List;

/**
 * Created by @author AlNat on 27.01.2022.
 * Licensed by Apache License, Version 2.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Utils {

    public static<T> List<T> getSubList(List<T> list, int maxValues) {
        return list.subList(0, maxValues);
    }

    public static String normalizeURL(String url) {
        return url;
    }

    @SneakyThrows
    public static String normalizeIP(String ip) {
        var cleared = ip.replaceAll("\\[\\]", "");
        return InetAddress.getByName(cleared).getHostAddress();
    }

    @SneakyThrows
    public static byte[] toByteArray(BufferedImage image) {
        return toByteArray(image, "png");
    }

    @SneakyThrows
    public static byte[] toByteArray(BufferedImage bi, String format) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, format, baos);
        return baos.toByteArray();
    }

    @SneakyThrows
    public static BufferedImage toBufferedImage(byte[] bytes) {
        InputStream is = new ByteArrayInputStream(bytes);
        return ImageIO.read(is);
    }

}
