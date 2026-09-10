package dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.cache.mapper;

import dev.pjosalgado.pocs.collectors.titleregistration.core.model.Imdb;
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.RottenTomatoes;
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.TitleEnrichmentData;
import dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.cache.entity.ImdbCacheEntity;
import dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.cache.entity.RottenTomatoesCacheEntity;
import dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.cache.entity.TitleEnrichmentDataCacheEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TitleCacheEntityMapper {

    TitleEnrichmentDataCacheEntity toCacheEntity(TitleEnrichmentData enrichmentData);

    TitleEnrichmentData toEnrichmentData(TitleEnrichmentDataCacheEntity cacheEntity);

    ImdbCacheEntity toImdbCacheEntity(Imdb imdb);

    Imdb toImdb(ImdbCacheEntity cacheEntity);

    RottenTomatoesCacheEntity toRottenTomatoesCacheEntity(RottenTomatoes rottenTomatoes);

    RottenTomatoes toRottenTomatoes(RottenTomatoesCacheEntity cacheEntity);

}
