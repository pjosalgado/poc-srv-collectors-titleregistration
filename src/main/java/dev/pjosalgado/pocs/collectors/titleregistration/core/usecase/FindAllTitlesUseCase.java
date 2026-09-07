package dev.pjosalgado.pocs.collectors.titleregistration.core.usecase;

import dev.pjosalgado.pocs.collectors.titleregistration.core.boundary.TitlePersistenceBoundary;
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.Title;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindAllTitlesUseCase {

    private final TitlePersistenceBoundary titlePersistenceBoundary;

    public Page<Title> execute(Pageable pageable) {
        return titlePersistenceBoundary.findAll(pageable);
    }

}
