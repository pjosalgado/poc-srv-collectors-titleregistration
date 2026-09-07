package dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.rest

import dev.pjosalgado.pocs.collectors.titleregistration.config.properties.OmdbProperties
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.TitleEnrichmentData
import dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.rest.client.OmdbClient
import dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.rest.dto.OmdbResponse
import dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.rest.mapper.OmdbResponseMapper
import spock.lang.Specification

class OmdbGatewaySpec extends Specification {

    def omdbClient = Mock(OmdbClient)
    def omdbProperties = Mock(OmdbProperties)
    def mapper = Mock(OmdbResponseMapper)
    def gateway = new OmdbGateway(omdbClient, omdbProperties, mapper)

    def "searchByTitle returns enrichment data when API returns valid response"() {
        given:
        def response = OmdbResponse.builder()
                .title("Princess Mononoke")
                .response("True")
                .build()
        def enrichmentData = TitleEnrichmentData.builder()
                .plot("A young boy encounters a befriended wolf.")
                .build()

        when:
        def result = gateway.searchByTitle("Princess Mononoke")

        then:
        1 * omdbProperties.getApiKey() >> "test-api-key"
        1 * omdbClient.searchByTitle("test-api-key", "Princess Mononoke") >> response
        1 * mapper.toTitleEnrichmentData(response) >> enrichmentData
        result.isPresent()
        result.get() == enrichmentData
    }

    def "searchByTitle returns empty when API returns null"() {
        when:
        def result = gateway.searchByTitle("Unknown Title")

        then:
        1 * omdbProperties.getApiKey() >> "test-api-key"
        1 * omdbClient.searchByTitle("test-api-key", "Unknown Title") >> null
        0 * mapper._
        !result.isPresent()
    }

    def "searchByTitle returns empty when API returns False response"() {
        given:
        def response = OmdbResponse.builder()
                .response("False")
                .build()

        when:
        def result = gateway.searchByTitle("Unknown Title")

        then:
        1 * omdbProperties.getApiKey() >> "test-api-key"
        1 * omdbClient.searchByTitle("test-api-key", "Unknown Title") >> response
        0 * mapper._
        !result.isPresent()
    }
}
