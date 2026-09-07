package dev.pjosalgado.pocs.collectors.titleregistration.core.usecase;

import dev.pjosalgado.pocs.collectors.titleregistration.core.boundary.TitlePersistenceBoundary;
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.Title;
import dev.pjosalgado.pocs.collectors.titleregistration.exceptions.model.TitleNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateTitleUseCase {

    private final TitlePersistenceBoundary titlePersistenceBoundary;

    public Title execute(String titleId, Title updates) {
        var existing = titlePersistenceBoundary.findById(titleId)
                .orElseThrow(() -> new TitleNotFoundException(titleId));
        existing.applyUpdates(updates);
        return titlePersistenceBoundary.update(existing);
    }

}
