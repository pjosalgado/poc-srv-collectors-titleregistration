package dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.messaging

import dev.pjosalgado.pocs.collectors.titleregistration.config.properties.QueueProperties
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.Title
import org.springframework.amqp.rabbit.core.RabbitTemplate
import spock.lang.Specification

class TitleEventsProducerSpec extends Specification {

    def rabbitTemplate = Mock(RabbitTemplate)
    def queueProperties = Mock(QueueProperties)
    def producer = new TitleEventsProducer(rabbitTemplate, queueProperties)

    def "publishTitleEnrichment sends message to correct queue"() {
        given:
        def title = Title.builder()
                .titleId("uuid-123")
                .name("Princess Mononoke")
                .originalName("Mononoke-hime")
                .studio("Studio Ghibli")
                .build()

        when:
        producer.publishTitleEnrichment(title)

        then:
        1 * queueProperties.getTitleEnrichment() >> "title.enrichment"
        1 * rabbitTemplate.convertAndSend("title.enrichment", {
            it.titleId() == "uuid-123" &&
            it.name() == "Princess Mononoke" &&
            it.originalName() == "Mononoke-hime" &&
            it.studio() == "Studio Ghibli"
        })
    }

    def "publishTitleEnrichment sends message with null optional fields"() {
        given:
        def title = Title.builder()
                .titleId("uuid-456")
                .name("Test Title")
                .build()

        when:
        producer.publishTitleEnrichment(title)

        then:
        1 * queueProperties.getTitleEnrichment() >> "title.enrichment"
        1 * rabbitTemplate.convertAndSend("title.enrichment", {
            it.titleId() == "uuid-456" &&
            it.name() == "Test Title" &&
            it.originalName() == null &&
            it.studio() == null
        })
    }
}
