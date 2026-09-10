package dev.pjosalgado.pocs.collectors.titleregistration.core.usecase;

import dev.pjosalgado.pocs.collectors.openapi.model.TitleKind;
import dev.pjosalgado.pocs.collectors.titleregistration.core.boundary.OmdbBoundary;
import dev.pjosalgado.pocs.collectors.titleregistration.core.boundary.TitleCacheBoundary;
import dev.pjosalgado.pocs.collectors.titleregistration.core.boundary.TitlePersistenceBoundary;
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.Title;
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.TitleEnrichmentData;
import dev.pjosalgado.pocs.collectors.titleregistration.core.record.TitleEnrichmentRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnrichTitleUseCase {

    private static final Set<TitleKind> SUPPORTED_CATEGORIES = Set.of(TitleKind.MOVIE, TitleKind.TV_SHOW);

    private final OmdbBoundary omdbBoundary;
    private final TitleCacheBoundary titleEnrichmentCache;
    private final TitlePersistenceBoundary titlePersistenceBoundary;

    public void execute(TitleEnrichmentRequest request) {

        if (request.titleCategory() == null || !SUPPORTED_CATEGORIES.contains(request.titleCategory())) {
            log.warn("Enrichment not supported for titleCategory: {} (titleId: {})", request.titleCategory(), request.titleId());
            return;
        }

        log.info("Enriching title: {} - {} / {}", request.titleId(), request.name(), request.originalName());

        Optional<TitleEnrichmentData> enrichmentData = searchWithFallback(request.originalName(), request.name());
        enrichmentData.ifPresent(data -> updateTitleWithEnrichmentData(request.titleId(), data));
    }

    private Optional<TitleEnrichmentData> searchWithFallback(String originalName, String name) {

        Optional<TitleEnrichmentData> result = searchByName(originalName);

        if (result.isPresent()) {
            return result;
        }

        if (!originalName.equals(name)) {
            return searchByName(name);
        }

        return Optional.empty();
    }

    private Optional<TitleEnrichmentData> searchByName(String name) {

        Optional<TitleEnrichmentData> cached = titleEnrichmentCache.get(name);

        if (cached.isPresent()) {
            log.info("Found enrichment data in cache for title: {}", name);
            return cached;
        }

        Optional<TitleEnrichmentData> fromApi = omdbBoundary.searchByTitle(name);

        fromApi.ifPresent(data -> {
            titleEnrichmentCache.put(name, data);
            log.info("Enrichment data retrieved from API and cached for title: {}", name);
        });

        return fromApi;
    }

    private void updateTitleWithEnrichmentData(String titleId, TitleEnrichmentData enrichmentData) {
        titlePersistenceBoundary.findById(titleId)
                .ifPresent(title -> {
                    var updates = Title.builder().enrichmentData(enrichmentData).build();
                    title.applyUpdates(updates);
                    titlePersistenceBoundary.update(title);
                });
    }

}
