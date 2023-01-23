package dev.alnat.tinylinkshortener.service;

import java.awt.image.BufferedImage;

/**
 * Created by @author AlNat on 24.01.2023.
 * Licensed by Apache License, Version 2.0
 */
public interface QRGenerator {

    BufferedImage generateQRCode(String shortLink);

}
