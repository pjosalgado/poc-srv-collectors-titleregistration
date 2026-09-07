package dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.rest;

import dev.pjosalgado.pocs.collectors.titleregistration.config.properties.OmdbProperties;
import dev.pjosalgado.pocs.collectors.titleregistration.core.boundary.OmdbBoundary;
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.TitleEnrichmentData;
import dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.rest.client.OmdbClient;
import dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.rest.dto.OmdbResponse;
import dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.rest.mapper.OmdbResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OmdbGateway implements OmdbBoundary {

    private final OmdbClient omdbClient;
    private final OmdbProperties omdbProperties;
    private final OmdbResponseMapper mapper;

    @Override
    public Optional<TitleEnrichmentData> searchByTitle(String title) {
        OmdbResponse response = omdbClient.searchByTitle(omdbProperties.getApiKey(), title);

        if (response == null || "False".equals(response.getResponse())) {
            return Optional.empty();
        }

        return Optional.of(mapper.toTitleEnrichmentData(response));
    }

}
