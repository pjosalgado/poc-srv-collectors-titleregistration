package dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.db;

import dev.pjosalgado.pocs.collectors.titleregistration.core.boundary.TitlePersistenceBoundary;
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.Title;
import dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.db.mapper.TitleEntityMapper;
import dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.db.repository.TitleMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TitlePersistenceGateway implements TitlePersistenceBoundary {

    private final TitleEntityMapper titleEntityMapper;
    private final TitleMongoRepository titleMongoRepository;

    @Override
    public Title create(Title title) {
        var entityToPersist = titleEntityMapper.toEntity(title);
        var saved = titleMongoRepository.save(entityToPersist);
        return titleEntityMapper.toTitle(saved);
    }

    @Override
    public Optional<Title> findById(String titleId) {
        return titleMongoRepository.findById(titleId)
                .map(titleEntityMapper::toTitle);
    }

    @Override
    public Page<Title> findAll(Pageable pageable) {
        return titleMongoRepository.findAll(pageable)
                .map(titleEntityMapper::toTitle);
    }

    @Override
    public Title update(Title title) {
        var entityToPersist = titleEntityMapper.toEntity(title);
        var saved = titleMongoRepository.save(entityToPersist);
        return titleEntityMapper.toTitle(saved);
    }

    @Override
    public void deleteById(String titleId) {
        titleMongoRepository.deleteById(titleId);
    }

}
