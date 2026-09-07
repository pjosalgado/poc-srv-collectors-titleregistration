package dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.cache;

import dev.pjosalgado.pocs.collectors.titleregistration.config.properties.CacheProperties;
import dev.pjosalgado.pocs.collectors.titleregistration.core.boundary.TitleCacheBoundary;
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.TitleEnrichmentData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TitleCacheGateway implements TitleCacheBoundary {

    private static final String CACHE_PREFIX = "title:enrichment:";

    private final CacheProperties cacheProperties;
    private final RedisTemplate<String, TitleEnrichmentData> redisTemplate;

    @Override
    public Optional<TitleEnrichmentData> get(String key) {
        TitleEnrichmentData cached = redisTemplate.opsForValue().get(CACHE_PREFIX + key);
        return Optional.ofNullable(cached);
    }

    @Override
    public void put(String key, TitleEnrichmentData value) {
        redisTemplate.opsForValue().set(CACHE_PREFIX + key, value, cacheProperties.getTitleEnrichment().getTtl());
    }

}
