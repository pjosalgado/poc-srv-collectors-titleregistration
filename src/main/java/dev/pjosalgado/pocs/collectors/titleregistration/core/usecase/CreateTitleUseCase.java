package dev.pjosalgado.pocs.collectors.titleregistration.core.usecase;

import dev.pjosalgado.pocs.collectors.titleregistration.core.boundary.TitleRegistrationBoundary;
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.Title;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateTitleUseCase {

    private final TitleRegistrationBoundary titleRegistrationBoundary;

    public Title execute(Title title) {
        title.generateId();
        return titleRegistrationBoundary.createTitle(title);
    }

}
