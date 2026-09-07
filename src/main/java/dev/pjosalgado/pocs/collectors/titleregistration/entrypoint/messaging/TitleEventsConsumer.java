package dev.pjosalgado.pocs.collectors.titleregistration.entrypoint.messaging;

import dev.pjosalgado.pocs.collectors.titleregistration.core.record.TitleEnrichmentRequest;
import dev.pjosalgado.pocs.collectors.titleregistration.core.usecase.EnrichTitleUseCase;
import dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.messaging.message.TitleEnrichmentMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.messaging.consumer.enabled", havingValue = "true")
public class TitleEventsConsumer {

    private final EnrichTitleUseCase enrichTitleUseCase;

    @RabbitListener(queues = "${app.queue.title-enrichment}")
    public void onMessage(TitleEnrichmentMessage message) {
        var request = new TitleEnrichmentRequest(
                message.getTitleId(),
                message.getName(),
                message.getOriginalName(),
                message.getStudio()
        );
        enrichTitleUseCase.execute(request);
    }

}
