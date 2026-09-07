package dev.pjosalgado.pocs.collectors.titleregistration.entrypoint.rest.mapper;

import dev.pjosalgado.pocs.collectors.openapi.model.TitleListItemEnrichmentDataResponse;
import dev.pjosalgado.pocs.collectors.openapi.model.TitleListItemResponse;
import dev.pjosalgado.pocs.collectors.titleregistration.entrypoint.rest.util.DateMappingUtils;
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.Title;
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.TitleEnrichmentData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.net.URI;

@Mapper(componentModel = "spring", uses = DateMappingUtils.class)
public interface TitleListItemMapper {

    @Mapping(target = "enrichmentData", source = "enrichmentData")
    TitleListItemResponse fromTitle(Title title);

    @Mapping(target = "posterUrl", qualifiedByName = "toUri")
    TitleListItemEnrichmentDataResponse fromEnrichmentData(TitleEnrichmentData enrichmentData);

    @Named("toUri")
    default URI toUri(String value) {
        return value != null ? URI.create(value) : null;
    }

}
