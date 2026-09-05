package dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.gateway;

import dev.pjosalgado.pocs.collectors.titleregistration.core.boundary.TitleRegistrationBoundary;
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.Title;
import dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.mapper.TitleEntityMapper;
import dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.repository.TitleMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TitleRegistrationGateway implements TitleRegistrationBoundary {

    private final TitleEntityMapper titleEntityMapper;
    private final TitleMongoRepository titleMongoRepository;

    @Override
    public Title createTitle(Title title) {
        var entityToPersist = titleEntityMapper.fromTitle(title);
        var saved = titleMongoRepository.save(entityToPersist);
        return titleEntityMapper.toTitle(saved);
    }

}
