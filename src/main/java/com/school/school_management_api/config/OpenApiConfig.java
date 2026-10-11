package com.school.school_management_api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.servlet.context-path:/api/v1}")
    private String contextPath;

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .servers(List.of(
                        new Server().url(contextPath).description("Servidor Local")
                ))
                .info(new Info()
                        .title("School Management REST API")
                        .version("v1.0.0")
                        .description("API para la gestión académica, control de usuarios y autenticación con JWT y Refresh Token.")
                        .contact(new Contact()
                                .name("Equipo de Desarrollo")
                                .email("dev@school.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP) // <-- CAMBIO AQUÍ: Se usa SecurityScheme.Type.HTTP
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Ingresa el Token JWT de acceso (sin el prefijo 'Bearer ')")));
    }
}