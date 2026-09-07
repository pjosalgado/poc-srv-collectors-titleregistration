package dev.pjosalgado.pocs.collectors.titleregistration.entrypoint.rest

import dev.pjosalgado.pocs.collectors.openapi.model.TitleCreateRequest
import dev.pjosalgado.pocs.collectors.openapi.model.TitleCreateWrapper
import dev.pjosalgado.pocs.collectors.openapi.model.TitleUpdateRequest
import dev.pjosalgado.pocs.collectors.openapi.model.TitleUpdateWrapper
import dev.pjosalgado.pocs.collectors.openapi.model.TitleCreatedResponse
import dev.pjosalgado.pocs.collectors.openapi.model.TitleDataWrapper
import dev.pjosalgado.pocs.collectors.openapi.model.TitleListItemResponse
import dev.pjosalgado.pocs.collectors.openapi.model.TitleType
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.Title
import dev.pjosalgado.pocs.collectors.titleregistration.core.usecase.CreateTitleUseCase
import dev.pjosalgado.pocs.collectors.titleregistration.core.usecase.DeleteTitleUseCase
import dev.pjosalgado.pocs.collectors.titleregistration.core.usecase.FindAllTitlesUseCase
import dev.pjosalgado.pocs.collectors.titleregistration.core.usecase.FindTitleByIdUseCase
import dev.pjosalgado.pocs.collectors.titleregistration.core.usecase.UpdateTitleUseCase
import dev.pjosalgado.pocs.collectors.titleregistration.entrypoint.rest.mapper.TitleListItemMapper
import dev.pjosalgado.pocs.collectors.titleregistration.entrypoint.rest.mapper.TitleRequestMapper
import dev.pjosalgado.pocs.collectors.titleregistration.entrypoint.rest.mapper.TitleResponseMapper
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import spock.lang.Specification

class TitleRegistrationControllerSpec extends Specification {

    def createUseCase = Mock(CreateTitleUseCase)
    def findUseCase = Mock(FindTitleByIdUseCase)
    def findAllUseCase = Mock(FindAllTitlesUseCase)
    def updateUseCase = Mock(UpdateTitleUseCase)
    def deleteUseCase = Mock(DeleteTitleUseCase)
    def requestMapper = Mock(TitleRequestMapper)
    def responseMapper = Mock(TitleResponseMapper)
    def listItemMapper = Mock(TitleListItemMapper)

    def controller = new TitleRegistrationController(
            createUseCase, findUseCase, findAllUseCase, updateUseCase, deleteUseCase,
            requestMapper, responseMapper, listItemMapper
    )

    def "titleCreate returns 201 with created title and links"() {
        given:
        def requestData = new TitleCreateRequest()
        def wrapper = new TitleCreateWrapper(data: requestData)
        def domainTitle = Title.builder().name("Test").build()
        def createdTitle = Title.builder().titleId("id-1").name("Test").build()
        def responseData = new TitleCreatedResponse()

        when:
        def result = controller.titleCreate(wrapper)

        then:
        1 * requestMapper.toTitle(requestData) >> domainTitle
        1 * createUseCase.execute(domainTitle) >> createdTitle
        1 * responseMapper.fromTitle(createdTitle) >> responseData
        result.getStatusCode().value() == 201
        result.getBody().getData() == responseData
        result.getBody().getLinks().containsKey("self")
        result.getBody().getLinks().containsKey("collection")
    }

    def "titleFindAll returns 200 with paginated titles and links"() {
        given:
        def title1 = Title.builder().titleId("id-1").name("Title 1").build()
        def title2 = Title.builder().titleId("id-2").name("Title 2").build()
        def responseData1 = new TitleListItemResponse(name: "Title 1")
        def responseData2 = new TitleListItemResponse(name: "Title 2")
        def pageable = PageRequest.of(0, 10)
        def page = new PageImpl<>([title1, title2], pageable, 2)

        when:
        def result = controller.titleFindAll(0, 10)

        then:
        1 * findAllUseCase.execute(pageable) >> page
        1 * listItemMapper.fromTitle(title1) >> responseData1
        1 * listItemMapper.fromTitle(title2) >> responseData2
        result.getStatusCode().value() == 200
        result.getBody().getData().size() == 2
        result.getBody().getLinks().containsKey("self")
        result.getBody().getLinks().containsKey("first")
        result.getBody().getLinks().containsKey("last")
    }

    def "titleFindAll includes prev link when not on first page"() {
        given:
        def pageable = PageRequest.of(1, 5)
        def page = new PageImpl<>([], pageable, 15)

        when:
        def result = controller.titleFindAll(1, 5)

        then:
        1 * findAllUseCase.execute(pageable) >> page
        result.getBody().getLinks().containsKey("prev")
        result.getBody().getLinks().containsKey("next")
        result.getBody().getLinks().containsKey("first")
        result.getBody().getLinks().containsKey("last")
    }

    def "titleFindAll uses defaults when parameters are null"() {
        given:
        def pageable = PageRequest.of(0, 10)
        def page = new PageImpl<>([], pageable, 0)

        when:
        def result = controller.titleFindAll(null, null)

        then:
        1 * findAllUseCase.execute(pageable) >> page
        result.getStatusCode().value() == 200
        result.getBody().getData().isEmpty()
    }

    def "titleFindById returns 200 with title and links"() {
        given:
        def title = Title.builder().titleId("id-1").build()
        def responseData = new TitleCreatedResponse()

        when:
        def result = controller.titleFindById("id-1")

        then:
        1 * findUseCase.execute("id-1") >> title
        1 * responseMapper.fromTitle(title) >> responseData
        result.getStatusCode().value() == 200
        result.getBody().getData() == responseData
        result.getBody().getLinks().containsKey("self")
        result.getBody().getLinks().containsKey("collection")
    }

    def "titleUpdate returns 200 with updated title and links"() {
        given:
        def requestData = new TitleUpdateRequest()
        requestData.setName("Updated")
        def wrapper = new TitleUpdateWrapper(data: requestData)
        def updatedTitle = Title.builder().titleId("id-1").name("Updated").build()
        def responseData = new TitleCreatedResponse()

        when:
        def result = controller.titleUpdate("id-1", wrapper)

        then:
        1 * updateUseCase.execute({ it.titleId == "id-1" && it.name == "Updated" }) >> updatedTitle
        1 * responseMapper.fromTitle(updatedTitle) >> responseData
        result.getStatusCode().value() == 200
        result.getBody().getData() == responseData
        result.getBody().getLinks().containsKey("self")
        result.getBody().getLinks().containsKey("collection")
    }

    def "titleDelete returns 204"() {
        when:
        def result = controller.titleDelete("id-1")

        then:
        1 * deleteUseCase.execute("id-1")
        result.getStatusCode().value() == 204
    }
}
