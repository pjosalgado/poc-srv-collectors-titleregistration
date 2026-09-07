package dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.db.mapper;

import dev.pjosalgado.pocs.collectors.titleregistration.entrypoint.rest.util.DateMappingUtils;
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.Title;
import dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.db.entity.TitleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = DateMappingUtils.class)
public interface TitleEntityMapper {

    TitleEntity toEntity(Title title);

    Title toTitle(TitleEntity titleEntity);

}
