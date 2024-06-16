package org.example.springboot.elasticsearch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI().info(new Info().title("Spring Data Elasticsearch example")
            .description("Spring Data Elasticsearch example with Testcontainers")
            .version("v0.0.2")
            .contact(getContactDetails())
            .license(getLicenseDetails()));
    }

    private Contact getContactDetails() {
        return new Contact().name("SA");
    }

    private License getLicenseDetails() {
        return new License().name("GNU General Public License v3.0")
            .url("https://github.com/skariajampi/spring3-elasticsearch.git");
    }
}