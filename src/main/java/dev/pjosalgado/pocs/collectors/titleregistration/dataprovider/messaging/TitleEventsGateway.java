package dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.messaging;

import dev.pjosalgado.pocs.collectors.titleregistration.config.properties.AppQueueProperties;
import dev.pjosalgado.pocs.collectors.titleregistration.core.boundary.TitleEventsBoundary;
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.Title;
import dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.messaging.message.TitleEnrichmentMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TitleEventsGateway implements TitleEventsBoundary {

    private final RabbitTemplate rabbitTemplate;
    private final AppQueueProperties appQueueProperties;

    @Override
    public void publishTitleEnrichment(Title title) {
        var message = TitleEnrichmentMessage.builder()
                .titleId(title.getTitleId())
                .name(title.getName())
                .originalName(title.getOriginalName())
                .studio(title.getStudio())
                .build();
        rabbitTemplate.convertAndSend(appQueueProperties.getTitleEnrichment(), message);
    }

}
