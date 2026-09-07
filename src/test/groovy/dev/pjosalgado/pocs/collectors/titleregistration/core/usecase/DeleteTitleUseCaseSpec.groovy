package dev.pjosalgado.pocs.collectors.titleregistration.core.usecase

import dev.pjosalgado.pocs.collectors.titleregistration.core.boundary.TitlePersistenceBoundary
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.Title
import dev.pjosalgado.pocs.collectors.titleregistration.exception.model.TitleNotFoundException
import spock.lang.Specification

class DeleteTitleUseCaseSpec extends Specification {

    def boundary = Mock(TitlePersistenceBoundary)
    def useCase = new DeleteTitleUseCase(boundary)

    def "execute deletes title when found"() {
        given:
        def title = Title.builder().titleId("id-1").build()

        when:
        useCase.execute("id-1")

        then:
        1 * boundary.findById("id-1") >> Optional.of(title)
        1 * boundary.deleteById("id-1")
    }

    def "execute throws TitleNotFoundException when not found"() {
        when:
        useCase.execute("missing-id")

        then:
        1 * boundary.findById("missing-id") >> Optional.empty()
        thrown(TitleNotFoundException)
        0 * boundary.deleteById(_)
    }
}
