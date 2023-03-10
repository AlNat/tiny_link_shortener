package dev.alnat.tinylinkshortener.usecase;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import dev.alnat.tinylinkshortener.configuration.PostgreSQLTestContainerConfiguration;
import dev.alnat.tinylinkshortener.dto.LinkInDTO;
import dev.alnat.tinylinkshortener.model.enums.LinkStatus;
import dev.alnat.tinylinkshortener.util.TestUtil;
import dev.alnat.tinylinkshortener.util.Utils;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Use case: create link and generate QR to it
 * <p>
 * Created by @author AlNat on 24.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@SpringBootTest(
        "custom.qr.url=http://localhost:80/s/%s" // override full endpoint to ensure qr link exactly the same
)
@ContextConfiguration(classes = PostgreSQLTestContainerConfiguration.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Order for test
@DirtiesContext
class QRGeneratorTest extends BaseMVCTest {

    private static final String FIRST_SHORT_LINK = "H4T";
    private static final byte[] SAVED_QR = TestUtil.readFromResourceFile("qr/qr.png");

    @Test
    @Order(1)
    @DisplayName("Step 1. Create the link")
    void create() {
        var link = new LinkInDTO();
        link.setOriginalLink("https://goggle.com");

        var result = saveNewLink(link);
        Assertions.assertEquals(200, result.getCode(), "Result code is not success!");
        Assertions.assertTrue(result.getData().getCreated().isBefore(LocalDateTime.now()), "Link not immediately saved!");
        Assertions.assertEquals(LinkStatus.CREATED, result.getData().getStatus(), "Link status not as new!");
        Assertions.assertEquals(0, result.getData().getCurrentVisitCount(), "Link already visited!");
        Assertions.assertEquals(FIRST_SHORT_LINK, result.getData().getShortLink(), "Short link not expected, is new engine?");
    }

    @Test
    @Order(2)
    @DisplayName("Step 2. Create the QR and validate it")
    void generateQR() throws Exception {
        var res = this.mvc.perform(
                        get("/api/v1/qr/" + FIRST_SHORT_LINK)
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentTypeCompatibleWith(MediaType.IMAGE_PNG))
                .andReturn()
                .getResponse().getContentAsByteArray();

        Assertions.assertArrayEquals(SAVED_QR, res, "QR code is not the same!");
    }

    @Test
    @Order(3)
    @DisplayName("Step 3. Decode the QR and validate it")
    void decodeQR() throws Exception {
        var bitmap = new BinaryBitmap(
                new HybridBinarizer(
                    new BufferedImageLuminanceSource(
                            Utils.toBufferedImage(SAVED_QR)
                    )
                )
        );

        var url = new QRCodeReader().decode(bitmap);

        Assertions.assertEquals("http://localhost:80/s/" + FIRST_SHORT_LINK, url.getText(),
                "QR code not encodes correctly!");
    }

}
