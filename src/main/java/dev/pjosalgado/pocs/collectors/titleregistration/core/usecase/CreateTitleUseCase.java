package dev.pjosalgado.pocs.collectors.titleregistration.core.usecase;

import dev.pjosalgado.pocs.collectors.titleregistration.core.boundary.TitlePersistenceBoundary;
import dev.pjosalgado.pocs.collectors.titleregistration.core.boundary.TitleEventsBoundary;
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.Title;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateTitleUseCase {

    private final TitlePersistenceBoundary titlePersistenceBoundary;
    private final TitleEventsBoundary titleEventsBoundary;

    public Title execute(Title title) {
        title.generateId();
        var titleCreated = titlePersistenceBoundary.create(title);
        titleEventsBoundary.publishTitleEnrichment(titleCreated);
        return titleCreated;
    }

}
