package dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.mapper;

import dev.pjosalgado.pocs.collectors.titleregistration.config.DateMappingUtil;
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.Title;
import dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.entity.TitleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = DateMappingUtil.class)
public interface TitleEntityMapper {

    TitleEntity fromTitle(Title title);

    Title toTitle(TitleEntity titleEntity);

}
