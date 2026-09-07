package dev.pjosalgado.pocs.collectors.titleregistration.core.boundary;

import dev.pjosalgado.pocs.collectors.titleregistration.core.model.TitleEnrichmentData;

import java.util.Optional;

public interface TitleCacheBoundary {

    Optional<TitleEnrichmentData> get(String key);

    void put(String key, TitleEnrichmentData value);

}
