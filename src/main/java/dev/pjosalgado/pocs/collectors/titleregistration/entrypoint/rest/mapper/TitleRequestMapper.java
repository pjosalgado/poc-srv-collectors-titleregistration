package dev.pjosalgado.pocs.collectors.titleregistration.entrypoint.rest.mapper;

import dev.pjosalgado.pocs.collectors.openapi.model.TitleCreateRequest;
import dev.pjosalgado.pocs.collectors.titleregistration.entrypoint.rest.util.DateMappingUtils;
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.Title;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = DateMappingUtils.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TitleRequestMapper {

    Title toTitle(TitleCreateRequest request);

}
