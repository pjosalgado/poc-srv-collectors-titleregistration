package dev.pjosalgado.pocs.collectors.titleregistration.entrypoint.rest.mapper;

import dev.pjosalgado.pocs.collectors.openapi.model.TitleCreatedResponse;
import dev.pjosalgado.pocs.collectors.titleregistration.entrypoint.rest.util.DateMappingUtils;
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.Title;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.net.URI;

@Mapper(componentModel = "spring", uses = DateMappingUtils.class)
public interface TitleResponseMapper {

    @Mapping(target = "enrichmentData.posterUrl", qualifiedByName = "toUri")
    TitleCreatedResponse fromTitle(Title title);

    @Named("toUri")
    default URI toUri(String value) {
        return value != null ? URI.create(value) : null;
    }

}
