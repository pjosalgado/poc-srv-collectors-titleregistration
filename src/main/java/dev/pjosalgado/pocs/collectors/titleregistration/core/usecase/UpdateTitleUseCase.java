package dev.pjosalgado.pocs.collectors.titleregistration.core.usecase;

import dev.pjosalgado.pocs.collectors.titleregistration.core.boundary.TitleEventsBoundary;
import dev.pjosalgado.pocs.collectors.titleregistration.core.boundary.TitlePersistenceBoundary;
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.Title;
import dev.pjosalgado.pocs.collectors.titleregistration.exception.model.TitleNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateTitleUseCase {

    private final TitlePersistenceBoundary titlePersistenceBoundary;
    private final TitleEventsBoundary titleEventsBoundary;

    public Title execute(Title updates) {
        var existing = titlePersistenceBoundary.findById(updates.getTitleId())
                .orElseThrow(() -> new TitleNotFoundException(updates.getTitleId()));

        boolean nameChanged = isNameChanged(existing, updates);
        boolean noEnrichmentData = existing.getEnrichmentData() == null;

        existing.applyUpdates(updates);

        if (nameChanged || noEnrichmentData) {
            existing.clearEnrichmentData();
        }

        var updated = titlePersistenceBoundary.update(existing);

        if (nameChanged || noEnrichmentData) {
            titleEventsBoundary.publishTitleEnrichment(updated);
        }

        return updated;
    }

    private boolean isNameChanged(Title existing, Title updates) {
        return (updates.getName() != null && !updates.getName().equals(existing.getName()))
                || (updates.getOriginalName() != null && !updates.getOriginalName().equals(existing.getOriginalName()));
    }

}
