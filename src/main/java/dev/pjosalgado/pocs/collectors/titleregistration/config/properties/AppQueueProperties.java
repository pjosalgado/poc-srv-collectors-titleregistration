package dev.pjosalgado.pocs.collectors.titleregistration.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.queue")
public class AppQueueProperties {

    private String titleEnrichment;

}
