package dev.pjosalgado.pocs.collectors.titleregistration.core.usecase

import dev.pjosalgado.pocs.collectors.openapi.model.TitleKind
import dev.pjosalgado.pocs.collectors.titleregistration.core.boundary.OmdbBoundary
import dev.pjosalgado.pocs.collectors.titleregistration.core.boundary.TitleCacheBoundary
import dev.pjosalgado.pocs.collectors.titleregistration.core.boundary.TitlePersistenceBoundary
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.Title
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.TitleEnrichmentData
import dev.pjosalgado.pocs.collectors.titleregistration.core.record.TitleEnrichmentRequest
import spock.lang.Specification

class EnrichTitleUseCaseSpec extends Specification {

    def omdbBoundary = Mock(OmdbBoundary)
    def titleCache = Mock(TitleCacheBoundary)
    def persistenceBoundary = Mock(TitlePersistenceBoundary)
    def useCase = new EnrichTitleUseCase(omdbBoundary, titleCache, persistenceBoundary)

    def "execute enriches title found by originalName from API"() {
        given:
        def request = new TitleEnrichmentRequest("id-1", "Mononoke-hime", "Princess Mononoke", "Studio Ghibli", TitleKind.MOVIE)
        def enrichmentData = TitleEnrichmentData.builder().plot("Test plot").build()
        def existingTitle = Title.builder().titleId("id-1").name("Princess Mononoke").originalName("Mononoke-hime").build()

        when:
        useCase.execute(request)

        then:
        1 * titleCache.get("Princess Mononoke") >> Optional.empty()
        1 * omdbBoundary.searchByTitle("Princess Mononoke") >> Optional.of(enrichmentData)
        1 * titleCache.put("Princess Mononoke", enrichmentData)
        1 * persistenceBoundary.findById("id-1") >> Optional.of(existingTitle)
        1 * persistenceBoundary.update({ it.getEnrichmentData() == enrichmentData })
    }

    def "execute uses cache hit for originalName"() {
        given:
        def request = new TitleEnrichmentRequest("id-1", "Mononoke-hime", "Princess Mononoke", "Studio Ghibli", TitleKind.MOVIE)
        def enrichmentData = TitleEnrichmentData.builder().plot("Cached plot").build()
        def existingTitle = Title.builder().titleId("id-1").name("Princess Mononoke").build()

        when:
        useCase.execute(request)

        then:
        1 * titleCache.get("Princess Mononoke") >> Optional.of(enrichmentData)
        0 * omdbBoundary._
        1 * persistenceBoundary.findById("id-1") >> Optional.of(existingTitle)
        1 * persistenceBoundary.update({ it.getEnrichmentData() == enrichmentData })
    }

    def "execute falls back to name when originalName not found"() {
        given:
        def request = new TitleEnrichmentRequest("id-1", "My Neighbor Totoro", "Tonari no Totoro", "Studio Ghibli", TitleKind.MOVIE)
        def enrichmentData = TitleEnrichmentData.builder().plot("Totoro plot").build()
        def existingTitle = Title.builder().titleId("id-1").name("My Neighbor Totoro").build()

        when:
        useCase.execute(request)

        then:
        1 * titleCache.get("Tonari no Totoro") >> Optional.empty()
        1 * omdbBoundary.searchByTitle("Tonari no Totoro") >> Optional.empty()
        1 * titleCache.get("My Neighbor Totoro") >> Optional.empty()
        1 * omdbBoundary.searchByTitle("My Neighbor Totoro") >> Optional.of(enrichmentData)
        1 * titleCache.put("My Neighbor Totoro", enrichmentData)
        1 * persistenceBoundary.findById("id-1") >> Optional.of(existingTitle)
        1 * persistenceBoundary.update({ it.getEnrichmentData() == enrichmentData })
    }

    def "execute does not call API when originalName equals name and not in cache"() {
        given:
        def request = new TitleEnrichmentRequest("id-1", "Same Name", "Same Name", "Studio", TitleKind.MOVIE)

        when:
        useCase.execute(request)

        then:
        1 * titleCache.get("Same Name") >> Optional.empty()
        1 * omdbBoundary.searchByTitle("Same Name") >> Optional.empty()
        0 * titleCache.get("Same Name") >> { throw new RuntimeException("Should not be called again") }
        0 * persistenceBoundary._
    }

    def "execute does not update when title not found in persistence"() {
        given:
        def request = new TitleEnrichmentRequest("missing-id", "Name", "Original Name", "Studio", TitleKind.MOVIE)
        def enrichmentData = TitleEnrichmentData.builder().plot("Plot").build()

        when:
        useCase.execute(request)

        then:
        1 * titleCache.get("Original Name") >> Optional.empty()
        1 * omdbBoundary.searchByTitle("Original Name") >> Optional.of(enrichmentData)
        1 * titleCache.put("Original Name", enrichmentData)
        1 * persistenceBoundary.findById("missing-id") >> Optional.empty()
        0 * persistenceBoundary.update(_)
    }

    def "execute does nothing when enrichment data not found"() {
        given:
        def request = new TitleEnrichmentRequest("id-1", "Same Name", "Same Name", "Studio", TitleKind.MOVIE)

        when:
        useCase.execute(request)

        then:
        1 * titleCache.get("Same Name") >> Optional.empty()
        1 * omdbBoundary.searchByTitle("Same Name") >> Optional.empty()
        0 * persistenceBoundary._
    }

    def "execute skips enrichment for DOCUMENTARY titleCategory"() {
        given:
        def request = new TitleEnrichmentRequest("id-1", "Name", "Original", "Studio", TitleKind.DOCUMENTARY)

        when:
        useCase.execute(request)

        then:
        0 * titleCache._
        0 * omdbBoundary._
        0 * persistenceBoundary._
    }

    def "execute skips enrichment for MUSIC_SHOW titleCategory"() {
        given:
        def request = new TitleEnrichmentRequest("id-1", "Name", "Original", "Studio", TitleKind.MUSIC_SHOW)

        when:
        useCase.execute(request)

        then:
        0 * titleCache._
        0 * omdbBoundary._
        0 * persistenceBoundary._
    }

    def "execute skips enrichment for null titleCategory"() {
        given:
        def request = new TitleEnrichmentRequest("id-1", "Name", "Original", "Studio", null)

        when:
        useCase.execute(request)

        then:
        0 * titleCache._
        0 * omdbBoundary._
        0 * persistenceBoundary._
    }

    def "execute enriches title with TV_SHOW titleCategory"() {
        given:
        def request = new TitleEnrichmentRequest("id-1", "Breaking Bad", "Breaking Bad", "AMC", TitleKind.TV_SHOW)
        def enrichmentData = TitleEnrichmentData.builder().plot("A high school teacher...").build()
        def existingTitle = Title.builder().titleId("id-1").name("Breaking Bad").build()

        when:
        useCase.execute(request)

        then:
        1 * titleCache.get("Breaking Bad") >> Optional.empty()
        1 * omdbBoundary.searchByTitle("Breaking Bad") >> Optional.of(enrichmentData)
        1 * titleCache.put("Breaking Bad", enrichmentData)
        1 * persistenceBoundary.findById("id-1") >> Optional.of(existingTitle)
        1 * persistenceBoundary.update({ it.getEnrichmentData() == enrichmentData })
    }
}
