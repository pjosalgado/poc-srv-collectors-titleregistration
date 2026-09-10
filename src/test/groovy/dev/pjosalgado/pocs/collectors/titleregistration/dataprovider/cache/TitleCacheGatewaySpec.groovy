package dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.cache

import dev.pjosalgado.pocs.collectors.titleregistration.config.properties.CacheProperties
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.TitleEnrichmentData
import dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.cache.entity.TitleEnrichmentDataCacheEntity
import dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.cache.mapper.TitleCacheEntityMapper
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import spock.lang.Specification

import java.time.Duration

class TitleCacheGatewaySpec extends Specification {

    def cacheProperties = Mock(CacheProperties)
    def redisTemplate = Mock(RedisTemplate)
    def valueOperations = Mock(ValueOperations)
    def mapper = Mock(TitleCacheEntityMapper)
    def gateway = new TitleCacheGateway(cacheProperties, redisTemplate, mapper)

    def "get returns cached enrichment data"() {
        given:
        def enrichmentData = TitleEnrichmentData.builder().plot("Cached plot").build()
        def cacheEntity = TitleEnrichmentDataCacheEntity.builder().plot("Cached plot").build()

        when:
        def result = gateway.get("Princess Mononoke")

        then:
        1 * redisTemplate.opsForValue() >> valueOperations
        1 * valueOperations.get("title:enrichment:Princess Mononoke") >> cacheEntity
        1 * mapper.toEnrichmentData(cacheEntity) >> enrichmentData
        result.isPresent()
        result.get() == enrichmentData
    }

    def "get returns empty when cache miss"() {
        when:
        def result = gateway.get("Unknown Title")

        then:
        1 * redisTemplate.opsForValue() >> valueOperations
        1 * valueOperations.get("title:enrichment:Unknown Title") >> null
        !result.isPresent()
    }

    def "put stores enrichment data with TTL"() {
        given:
        def enrichmentData = TitleEnrichmentData.builder().plot("New plot").build()
        def cacheEntity = TitleEnrichmentDataCacheEntity.builder().plot("New plot").build()
        def titleEnrichmentProps = Mock(CacheProperties.TitleEnrichmentProperties)
        titleEnrichmentProps.getTtl() >> Duration.ofHours(24)

        when:
        gateway.put("Princess Mononoke", enrichmentData)

        then:
        1 * mapper.toCacheEntity(enrichmentData) >> cacheEntity
        1 * cacheProperties.getTitleEnrichment() >> titleEnrichmentProps
        1 * redisTemplate.opsForValue() >> valueOperations
        1 * valueOperations.set("title:enrichment:Princess Mononoke", cacheEntity, Duration.ofHours(24))
    }
}
