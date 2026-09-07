package dev.pjosalgado.pocs.collectors.titleregistration.core.usecase

import dev.pjosalgado.pocs.collectors.titleregistration.core.boundary.TitlePersistenceBoundary
import dev.pjosalgado.pocs.collectors.titleregistration.core.boundary.TitleEventsBoundary
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.Title
import spock.lang.Specification

class CreateTitleUseCaseSpec extends Specification {

    def boundary = Mock(TitlePersistenceBoundary)
    def eventPublisher = Mock(TitleEventsBoundary)
    def useCase = new CreateTitleUseCase(boundary, eventPublisher)

    def "execute generates id, creates title and publishes enrichment message"() {
        given:
        def title = Title.builder().name("Test").build()

        when:
        def result = useCase.execute(title)

        then:
        1 * boundary.create({ it.getTitleId() != null && it.getName() == "Test" }) >> { Title t -> t }
        1 * eventPublisher.publishTitleEnrichment(_)
        result.getTitleId() != null
    }
}
