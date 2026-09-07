package dev.pjosalgado.pocs.collectors.titleregistration.core.usecase;

import dev.pjosalgado.pocs.collectors.titleregistration.core.boundary.TitlePersistenceBoundary;
import dev.pjosalgado.pocs.collectors.titleregistration.exception.model.TitleNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteTitleUseCase {

    private final TitlePersistenceBoundary titlePersistenceBoundary;

    public void execute(String titleId) {
        if (titlePersistenceBoundary.findById(titleId).isEmpty()) {
            throw new TitleNotFoundException(titleId);
        }
        titlePersistenceBoundary.deleteById(titleId);
    }

}
