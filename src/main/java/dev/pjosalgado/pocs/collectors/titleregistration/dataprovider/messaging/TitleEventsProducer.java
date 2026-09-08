package dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.messaging;

import dev.pjosalgado.pocs.collectors.asyncapi.model.TitleEnrichmentPayload;
import dev.pjosalgado.pocs.collectors.titleregistration.config.properties.QueueProperties;
import dev.pjosalgado.pocs.collectors.titleregistration.core.boundary.TitleEventsBoundary;
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.Title;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TitleEventsProducer implements TitleEventsBoundary {

    private final RabbitTemplate rabbitTemplate;
    private final QueueProperties queueProperties;

    @Override
    public void publishTitleEnrichment(Title title) {
        var message = new TitleEnrichmentPayload(
                title.getTitleId(),
                title.getName(),
                title.getOriginalName(),
                title.getStudio(),
                title.getTitleCategory() != null ? title.getTitleCategory().getValue() : null
        );
        rabbitTemplate.convertAndSend(queueProperties.getTitleEnrichment(), message);
    }

}
