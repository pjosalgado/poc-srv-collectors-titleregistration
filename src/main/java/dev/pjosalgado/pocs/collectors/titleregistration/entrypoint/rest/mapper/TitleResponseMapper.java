package dev.pjosalgado.pocs.collectors.titleregistration.entrypoint.rest.mapper;

import dev.pjosalgado.pocs.collectors.openapi.model.TitleCreatedResponse;
import dev.pjosalgado.pocs.collectors.titleregistration.entrypoint.rest.util.DateMappingUtils;
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.Title;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = DateMappingUtils.class)
public interface TitleResponseMapper {

    TitleCreatedResponse fromTitle(Title title);

}
