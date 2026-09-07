package dev.pjosalgado.pocs.collectors.titleregistration.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.cache")
public class CacheProperties {

    private TitleEnrichmentProperties titleEnrichment;

    @Getter
    @Setter
    public static class TitleEnrichmentProperties {

        private Duration ttl;

    }

}
