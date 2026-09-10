package dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.cache;

import dev.pjosalgado.pocs.collectors.titleregistration.config.properties.CacheProperties;
import dev.pjosalgado.pocs.collectors.titleregistration.core.boundary.TitleCacheBoundary;
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.TitleEnrichmentData;
import dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.cache.entity.TitleEnrichmentDataCacheEntity;
import dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.cache.mapper.TitleCacheEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TitleCacheGateway implements TitleCacheBoundary {

    private static final String CACHE_PREFIX = "title:enrichment:";

    private final CacheProperties cacheProperties;
    private final RedisTemplate<String, TitleEnrichmentDataCacheEntity> redisTemplate;
    private final TitleCacheEntityMapper mapper;

    @Override
    public Optional<TitleEnrichmentData> get(String key) {
        TitleEnrichmentDataCacheEntity cached = redisTemplate.opsForValue().get(CACHE_PREFIX + key);
        return Optional.ofNullable(cached).map(mapper::toEnrichmentData);
    }

    @Override
    public void put(String key, TitleEnrichmentData value) {
        TitleEnrichmentDataCacheEntity entity = mapper.toCacheEntity(value);
        redisTemplate.opsForValue().set(CACHE_PREFIX + key, entity, cacheProperties.getTitleEnrichment().getTtl());
    }

}
