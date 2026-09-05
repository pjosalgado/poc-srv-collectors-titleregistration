package dev.pjosalgado.pocs.collectors.titleregistration.config;

import org.springdoc.core.configuration.SpringDocConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = SpringDocConfiguration.class,
        basePackages = "dev.pjosalgado.pocs.collectors.openapi.api")
public class OpenApiConfiguration {
}
