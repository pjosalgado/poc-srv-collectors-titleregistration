package dev.pjosalgado.pocs.collectors.titleregistration.core.boundary;

import dev.pjosalgado.pocs.collectors.titleregistration.core.model.TitleEnrichmentData;

import java.util.Optional;

public interface OmdbBoundary {

    Optional<TitleEnrichmentData> searchByTitle(String title);

}
