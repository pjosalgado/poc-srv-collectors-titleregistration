package dev.pjosalgado.pocs.collectors.titleregistration.core.usecase;

import dev.pjosalgado.pocs.collectors.titleregistration.core.boundary.TitleBoundary;
import dev.pjosalgado.pocs.collectors.titleregistration.exceptions.model.TitleNotFoundException;
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.Title;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindTitleByIdUseCase {

    private final TitleBoundary titleBoundary;

    public Title execute(String titleId) {
        return titleBoundary.findById(titleId)
                .orElseThrow(() -> new TitleNotFoundException(titleId));
    }

}
