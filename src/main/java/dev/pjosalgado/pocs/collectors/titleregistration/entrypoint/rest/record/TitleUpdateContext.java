package dev.pjosalgado.pocs.collectors.titleregistration.entrypoint.rest.record;

import dev.pjosalgado.pocs.collectors.openapi.model.TitleUpdateRequest;

public record TitleUpdateContext(
        String titleId,
        TitleUpdateRequest updates
) {
}
