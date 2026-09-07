package dev.pjosalgado.pocs.collectors.titleregistration.core.boundary;

import dev.pjosalgado.pocs.collectors.titleregistration.core.model.Title;

public interface TitleEventsBoundary {

    void publishTitleEnrichment(Title title);

}
