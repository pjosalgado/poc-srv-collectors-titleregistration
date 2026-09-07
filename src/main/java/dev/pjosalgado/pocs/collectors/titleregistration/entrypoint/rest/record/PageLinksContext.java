package dev.pjosalgado.pocs.collectors.titleregistration.entrypoint.rest.record;

import dev.pjosalgado.pocs.collectors.openapi.model.Link;

import java.util.Map;

public record PageLinksContext(
        int currentPage,
        int pageSize,
        int totalPages,
        Map<String, Link> links
) {
}
