package dev.pjosalgado.pocs.collectors.titleregistration.config;

import dev.pjosalgado.pocs.collectors.titleregistration.core.model.TitleEnrichmentData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, TitleEnrichmentData> titleEnrichmentRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, TitleEnrichmentData> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        var serializer = RedisSerializer.json();

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);
        template.afterPropertiesSet();

        return template;
    }

}
