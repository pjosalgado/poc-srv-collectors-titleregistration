package dev.pjosalgado.pocs.collectors.titleregistration.config;

import dev.pjosalgado.pocs.collectors.titleregistration.config.properties.AppQueueProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AppQueueProperties.class)
@RequiredArgsConstructor
public class RabbitMQConfiguration {

    private final AppQueueProperties appQueueProperties;

    @Bean
    public Queue titleEnrichmentQueue() {
        return new Queue(appQueueProperties.getTitleEnrichment(), true);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new JacksonJsonMessageConverter());
        return rabbitTemplate;
    }

}
