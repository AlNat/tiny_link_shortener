package dev.alnat.tinylinkshortener.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.DateTimeSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * Created by @author AlNat on 16.01.2023.
 * Licensed by Apache License, Version 2.0
 */
@Configuration
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Tiny Link Shortener")
                        .version("1.0.0")
                        .description("REST API for create, view and analytics short links"));
    }

    @Bean
    public OpenApiCustomiser openAPICustomiser() {
        return openApi -> {
            openApi.getComponents().getSchemas().forEach((s, schema) -> {
                Map<String, Schema> properties = schema.getProperties();
                if (properties == null) {
                    properties = Map.of();
                }

                for (var property : properties.entrySet()) {
                    // Swagger datetime example change for date time properties
                    Schema propertySchema = property.getValue();
                    if (propertySchema instanceof DateTimeSchema) {
                        properties.replace(property.getKey(), new StringSchema()
                                .example("2023-01-01 10:00:00")
                                .description(propertySchema.getDescription()));
                    }
                }
            });
        };
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .displayName("public_api")
                .pathsToExclude("/api/**")
                .build();
    }

    @Bean
    public GroupedOpenApi privateOpenApi() {
        return GroupedOpenApi.builder()
                .group("private")
                .displayName("private_api")
                .pathsToMatch("/api/**")
                .build();
    }

}
