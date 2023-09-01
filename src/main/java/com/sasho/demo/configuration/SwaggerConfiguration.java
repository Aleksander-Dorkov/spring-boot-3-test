package com.sasho.demo.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(security = {@SecurityRequirement(name = "bearer-key")})
public class SwaggerConfiguration {
    @Bean
    public OpenAPI customOpenAPI() {
        License license = new License()
                .name("Apache 2.0")
                .url("http://springdoc.org");
        Info info = new Info()
                .title("BackEnd API")
                .version("1.0")
                .description("Back end api with JWT security example")
                .termsOfService("http://swagger.io/terms/")
                .license(license);
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");
        Components components = new Components().addSecuritySchemes("bearer-key", securityScheme);
        return new OpenAPI().components(components).info(info);
    }

}
