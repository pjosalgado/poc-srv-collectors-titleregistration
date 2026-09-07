package dev.pjosalgado.pocs.collectors.titleregistration.exception.handler

import dev.pjosalgado.pocs.collectors.titleregistration.exception.model.TitleNotFoundException
import spock.lang.Specification

class TitleExceptionHandlerSpec extends Specification {

    def handler = new TitleExceptionHandler()

    def "handleNotFound returns 404 with error list"() {
        given:
        def exception = new TitleNotFoundException("id-1")

        when:
        def result = handler.handleNotFound(exception)

        then:
        result.getStatusCode().value() == 404
        result.getBody().getErrors().size() == 1
        result.getBody().getErrors().get(0).getCode() == "NOT_FOUND"
        result.getBody().getErrors().get(0).getDetail() == "Title not found: id-1"
    }
}
