package dev.pjosalgado.pocs.collectors.titleregistration.core.usecase;

import dev.pjosalgado.pocs.collectors.titleregistration.core.boundary.TitlePersistenceBoundary;
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.Title;
import dev.pjosalgado.pocs.collectors.titleregistration.exception.model.TitleNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateTitleUseCase {

    private final TitlePersistenceBoundary titlePersistenceBoundary;

    public Title execute(Title updates) {
        var existing = titlePersistenceBoundary.findById(updates.getTitleId())
                .orElseThrow(() -> new TitleNotFoundException(updates.getTitleId()));
        existing.applyUpdates(updates);
        return titlePersistenceBoundary.update(existing);
    }

}
