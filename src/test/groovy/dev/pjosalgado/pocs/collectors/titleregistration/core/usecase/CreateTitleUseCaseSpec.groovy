package dev.pjosalgado.pocs.collectors.titleregistration.core.usecase

import dev.pjosalgado.pocs.collectors.titleregistration.core.boundary.TitleBoundary
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.Title
import spock.lang.Specification

class CreateTitleUseCaseSpec extends Specification {

    def boundary = Mock(TitleBoundary)
    def useCase = new CreateTitleUseCase(boundary)

    def "execute generates id and creates title"() {
        given:
        def title = Title.builder().name("Test").build()

        when:
        def result = useCase.execute(title)

        then:
        1 * boundary.create({ it.getTitleId() != null && it.getName() == "Test" }) >> { Title t -> t }
        result.getTitleId() != null
    }
}
