package dev.pjosalgado.pocs.collectors.titleregistration.core.usecase;

import dev.pjosalgado.pocs.collectors.titleregistration.core.boundary.TitleBoundary;
import dev.pjosalgado.pocs.collectors.titleregistration.exceptions.model.TitleNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteTitleUseCase {

    private final TitleBoundary titleBoundary;

    public void execute(String titleId) {
        if (titleBoundary.findById(titleId).isEmpty()) {
            throw new TitleNotFoundException(titleId);
        }
        titleBoundary.deleteById(titleId);
    }

}
