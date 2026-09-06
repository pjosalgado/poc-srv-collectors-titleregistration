package dev.pjosalgado.pocs.collectors.titleregistration.core.usecase;

import dev.pjosalgado.pocs.collectors.titleregistration.core.boundary.TitleBoundary;
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.Title;
import dev.pjosalgado.pocs.collectors.titleregistration.exceptions.model.TitleNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateTitleUseCase {

    private final TitleBoundary titleBoundary;

    public Title execute(String titleId, Title updates) {
        var existing = titleBoundary.findById(titleId)
                .orElseThrow(() -> new TitleNotFoundException(titleId));
        existing.applyUpdates(updates);
        return titleBoundary.update(existing);
    }

}
