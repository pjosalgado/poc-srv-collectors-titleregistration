package dev.pjosalgado.pocs.collectors.titleregistration.core.record;

public record TitleEnrichmentRequest(
        String titleId,
        String name,
        String originalName,
        String studio
) {
}
