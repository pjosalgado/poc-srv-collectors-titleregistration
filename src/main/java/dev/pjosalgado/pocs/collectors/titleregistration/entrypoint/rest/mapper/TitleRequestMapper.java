package dev.pjosalgado.pocs.collectors.titleregistration.entrypoint.rest.mapper;

import dev.pjosalgado.pocs.collectors.openapi.model.TitleCreateRequest;
import dev.pjosalgado.pocs.collectors.titleregistration.config.DateMappingUtil;
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.Title;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = DateMappingUtil.class)
public interface TitleRequestMapper {

    Title toTitle(TitleCreateRequest request);

}
