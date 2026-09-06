package dev.pjosalgado.pocs.collectors.titleregistration.core.usecase

import dev.pjosalgado.pocs.collectors.titleregistration.core.boundary.TitleBoundary
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.Title
import dev.pjosalgado.pocs.collectors.titleregistration.exceptions.model.TitleNotFoundException
import spock.lang.Specification

class FindTitleByIdUseCaseSpec extends Specification {

    def boundary = Mock(TitleBoundary)
    def useCase = new FindTitleByIdUseCase(boundary)

    def "execute returns title when found"() {
        given:
        def title = Title.builder().titleId("id-1").name("Test").build()

        when:
        def result = useCase.execute("id-1")

        then:
        1 * boundary.findById("id-1") >> Optional.of(title)
        result == title
    }

    def "execute throws TitleNotFoundException when not found"() {
        when:
        useCase.execute("missing-id")

        then:
        1 * boundary.findById("missing-id") >> Optional.empty()
        thrown(TitleNotFoundException)
    }
}
