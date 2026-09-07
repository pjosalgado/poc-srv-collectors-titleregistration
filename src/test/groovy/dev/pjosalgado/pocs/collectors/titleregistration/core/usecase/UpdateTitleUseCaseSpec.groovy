package dev.pjosalgado.pocs.collectors.titleregistration.core.usecase

import dev.pjosalgado.pocs.collectors.titleregistration.core.boundary.TitleEventsBoundary
import dev.pjosalgado.pocs.collectors.titleregistration.core.boundary.TitlePersistenceBoundary
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.Title
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.TitleEnrichmentData
import dev.pjosalgado.pocs.collectors.titleregistration.exception.model.TitleNotFoundException
import spock.lang.Specification

class UpdateTitleUseCaseSpec extends Specification {

    def boundary = Mock(TitlePersistenceBoundary)
    def eventPublisher = Mock(TitleEventsBoundary)
    def useCase = new UpdateTitleUseCase(boundary, eventPublisher)

    def "execute updates existing title without name change does not publish message"() {
        given:
        def enrichmentData = TitleEnrichmentData.builder().plot("Plot").build()
        def existing = Title.builder().titleId("id-1").name("Old").studio("Old Studio").enrichmentData(enrichmentData).build()
        def updates = Title.builder().titleId("id-1").studio("New Studio").build()

        when:
        def result = useCase.execute(updates)

        then:
        1 * boundary.findById("id-1") >> Optional.of(existing)
        1 * boundary.update(_) >> { Title t -> t }
        0 * eventPublisher._
        result.getStudio() == "New Studio"
        result.getEnrichmentData() == enrichmentData
    }

    def "execute clears enrichmentData and publishes message when name changes"() {
        given:
        def enrichmentData = TitleEnrichmentData.builder().plot("Plot").build()
        def existing = Title.builder().titleId("id-1").name("Old").enrichmentData(enrichmentData).build()
        def updates = Title.builder().titleId("id-1").name("New").build()

        when:
        def result = useCase.execute(updates)

        then:
        1 * boundary.findById("id-1") >> Optional.of(existing)
        1 * boundary.update(_) >> { Title t -> t }
        1 * eventPublisher.publishTitleEnrichment(_)
        result.getName() == "New"
        result.getEnrichmentData() == null
    }

    def "execute clears enrichmentData and publishes message when originalName changes"() {
        given:
        def enrichmentData = TitleEnrichmentData.builder().plot("Plot").build()
        def existing = Title.builder().titleId("id-1").name("Name").originalName("Old Original").enrichmentData(enrichmentData).build()
        def updates = Title.builder().titleId("id-1").originalName("New Original").build()

        when:
        def result = useCase.execute(updates)

        then:
        1 * boundary.findById("id-1") >> Optional.of(existing)
        1 * boundary.update(_) >> { Title t -> t }
        1 * eventPublisher.publishTitleEnrichment(_)
        result.getOriginalName() == "New Original"
        result.getEnrichmentData() == null
    }

    def "execute publishes message when enrichmentData is null"() {
        given:
        def existing = Title.builder().titleId("id-1").name("Name").build()
        def updates = Title.builder().titleId("id-1").studio("Studio").build()

        when:
        def result = useCase.execute(updates)

        then:
        1 * boundary.findById("id-1") >> Optional.of(existing)
        1 * boundary.update(_) >> { Title t -> t }
        1 * eventPublisher.publishTitleEnrichment(_)
        result.getStudio() == "Studio"
    }

    def "execute throws TitleNotFoundException when not found"() {
        given:
        def updates = Title.builder().titleId("missing-id").name("New").build()

        when:
        useCase.execute(updates)

        then:
        1 * boundary.findById("missing-id") >> Optional.empty()
        thrown(TitleNotFoundException)
    }
}
