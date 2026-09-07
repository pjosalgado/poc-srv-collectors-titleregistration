package dev.pjosalgado.pocs.collectors.titleregistration.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.queue")
public class QueueProperties {

    private String titleEnrichment;
    private String dlqSuffix;

    public String getTitleEnrichmentDlq() {
        return titleEnrichment + dlqSuffix;
    }

}
