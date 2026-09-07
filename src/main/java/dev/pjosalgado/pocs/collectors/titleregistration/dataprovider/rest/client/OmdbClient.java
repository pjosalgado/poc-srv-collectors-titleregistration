package dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.rest.client;

import dev.pjosalgado.pocs.collectors.titleregistration.config.properties.OmdbProperties;
import dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.rest.dto.OmdbResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class OmdbClient {

    private final RestClient restClient;

    public OmdbClient(OmdbProperties properties) {
        this.restClient = RestClient.builder()
                .baseUrl(properties.getBaseUrl())
                .build();
    }

    public OmdbResponse searchByTitle(String apiKey, String title) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("apikey", apiKey)
                        .queryParam("t", title)
                        .build())
                .retrieve()
                .body(OmdbResponse.class);
    }

}
