package dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.db.mapper;

import dev.pjosalgado.pocs.collectors.titleregistration.core.model.Imdb;
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.RottenTomatoes;
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.Title;
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.TitleEnrichmentData;
import dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.db.entity.ImdbEntity;
import dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.db.entity.RottenTomatoesEntity;
import dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.db.entity.TitleEnrichmentDataEntity;
import dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.db.entity.TitleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TitleEntityMapper {

    TitleEntity toEntity(Title title);

    Title toTitle(TitleEntity titleEntity);

    TitleEnrichmentDataEntity toEnrichmentDataEntity(TitleEnrichmentData enrichmentData);

    TitleEnrichmentData toEnrichmentData(TitleEnrichmentDataEntity entity);

    ImdbEntity toImdbEntity(Imdb imdb);

    Imdb toImdb(ImdbEntity entity);

    RottenTomatoesEntity toRottenTomatoesEntity(RottenTomatoes rottenTomatoes);

    RottenTomatoes toRottenTomatoes(RottenTomatoesEntity entity);

}
