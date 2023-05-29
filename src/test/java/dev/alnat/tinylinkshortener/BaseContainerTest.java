package dev.alnat.tinylinkshortener;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public abstract class BaseContainerTest {

    private static final String POSTGRES_DOCKER_NAME = "postgres:15.1-alpine3.17"; // PG last version

    protected static final PostgreSQLContainer<?> postgresql;

    static {
        postgresql = new PostgreSQLContainer<>(DockerImageName.parse(POSTGRES_DOCKER_NAME))
                .withExposedPorts(5432)
                .withReuse(true);
        postgresql.start();
    }

    @DynamicPropertySource
    protected static void registerPropertiesFromContainers(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () ->
                String.format(
                        "jdbc:postgresql://%s:%d/%s?loggerLevel=OFF&currentSchema=tiny_link_shortener",
                        postgresql.getHost(),
                        postgresql.getMappedPort(5432),
                        postgresql.getDatabaseName()
                )
        );
        registry.add("spring.datasource.username", postgresql::getUsername);
        registry.add("spring.datasource.password", postgresql::getPassword);
    }

}
