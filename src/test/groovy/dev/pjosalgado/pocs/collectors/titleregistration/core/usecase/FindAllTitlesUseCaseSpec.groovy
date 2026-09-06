package dev.pjosalgado.pocs.collectors.titleregistration.core.usecase

import dev.pjosalgado.pocs.collectors.titleregistration.core.boundary.TitleBoundary
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.Title
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import spock.lang.Specification

class FindAllTitlesUseCaseSpec extends Specification {

    def titleBoundary = Mock(TitleBoundary)
    def useCase = new FindAllTitlesUseCase(titleBoundary)

    def "execute returns paginated titles"() {
        given:
        def title1 = Title.builder().titleId("id-1").name("Title 1").build()
        def title2 = Title.builder().titleId("id-2").name("Title 2").build()
        def pageable = PageRequest.of(0, 10)
        def page = new PageImpl<>([title1, title2], pageable, 2)

        when:
        def result = useCase.execute(pageable)

        then:
        1 * titleBoundary.findAll(pageable) >> page
        result.getContent().size() == 2
        result.getNumber() == 0
        result.getSize() == 10
        result.getTotalElements() == 2
    }

    def "execute returns empty page when no titles exist"() {
        given:
        def pageable = PageRequest.of(0, 10)
        def page = new PageImpl<>([], pageable, 0)

        when:
        def result = useCase.execute(pageable)

        then:
        1 * titleBoundary.findAll(pageable) >> page
        result.getContent().isEmpty()
        result.getTotalElements() == 0
    }

    def "execute respects page parameters"() {
        given:
        def pageable = PageRequest.of(2, 5)
        def page = new PageImpl<>([], pageable, 15)

        when:
        def result = useCase.execute(pageable)

        then:
        1 * titleBoundary.findAll(pageable) >> page
        result.getNumber() == 2
        result.getSize() == 5
        result.getTotalElements() == 15
    }
}
