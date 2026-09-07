package dev.pjosalgado.pocs.collectors.titleregistration.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.omdb")
public class OmdbProperties {

    private String apiKey;
    private String baseUrl;

}
