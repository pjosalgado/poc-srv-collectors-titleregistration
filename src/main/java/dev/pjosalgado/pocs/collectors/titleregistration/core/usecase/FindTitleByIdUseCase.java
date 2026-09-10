package dev.pjosalgado.pocs.collectors.titleregistration.core.usecase;

import dev.pjosalgado.pocs.collectors.titleregistration.core.boundary.TitlePersistenceBoundary;
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.Title;
import dev.pjosalgado.pocs.collectors.titleregistration.exception.model.TitleNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindTitleByIdUseCase {

    private final TitlePersistenceBoundary titlePersistenceBoundary;

    public Title execute(String titleId) {
        return titlePersistenceBoundary.findById(titleId)
                .orElseThrow(() -> new TitleNotFoundException(titleId));
    }

}
