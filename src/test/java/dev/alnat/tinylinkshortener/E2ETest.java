package dev.alnat.tinylinkshortener;

import dev.alnat.tinylinkshortener.configuration.PostgreSQLTestContainerConfiguration;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

/**
 * Custom test annotation to simplify test creation
 * Create Postgres testcontainer
 *
 * Created by @author AlNat on 26.02.2023.
 * Licensed by Apache License, Version 2.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ContextConfiguration(classes = PostgreSQLTestContainerConfiguration.class)
@AutoConfigureMockMvc
@DirtiesContext
@SpringBootTest
@Transactional(propagation = NOT_SUPPORTED)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Order for test
public @interface E2ETest {



}
