package dev.pjosalgado.pocs.collectors.titleregistration.core.record;

import dev.pjosalgado.pocs.collectors.openapi.model.TitleKind;

public record TitleEnrichmentRequest(
        String titleId,
        String name,
        String originalName,
        String studio,
        TitleKind titleCategory
) {
}
