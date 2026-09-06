package dev.pjosalgado.pocs.collectors.titleregistration.core.usecase

import dev.pjosalgado.pocs.collectors.titleregistration.core.boundary.TitleBoundary
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.Title
import dev.pjosalgado.pocs.collectors.titleregistration.exceptions.model.TitleNotFoundException
import spock.lang.Specification

class UpdateTitleUseCaseSpec extends Specification {

    def boundary = Mock(TitleBoundary)
    def useCase = new UpdateTitleUseCase(boundary)

    def "execute updates existing title"() {
        given:
        def existing = Title.builder().titleId("id-1").name("Old").studio("Old Studio").build()
        def updates = Title.builder().name("New").build()

        when:
        def result = useCase.execute("id-1", updates)

        then:
        1 * boundary.findById("id-1") >> Optional.of(existing)
        1 * boundary.update({ it.getName() == "New" && it.getStudio() == "Old Studio" }) >> { Title t -> t }
        result.getName() == "New"
        result.getStudio() == "Old Studio"
    }

    def "execute throws TitleNotFoundException when not found"() {
        given:
        def updates = Title.builder().name("New").build()

        when:
        useCase.execute("missing-id", updates)

        then:
        1 * boundary.findById("missing-id") >> Optional.empty()
        thrown(TitleNotFoundException)
    }
}
