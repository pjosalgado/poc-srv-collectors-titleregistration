package dev.pjosalgado.pocs.collectors.titleregistration.entrypoint.rest.mapper

import dev.pjosalgado.pocs.collectors.openapi.model.MonetaryType
import dev.pjosalgado.pocs.collectors.openapi.model.PurchaseDetailsRequest
import dev.pjosalgado.pocs.collectors.openapi.model.TitleCreateRequest
import dev.pjosalgado.pocs.collectors.openapi.model.TitleType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class TitleRequestMapperSpec extends Specification {

    @Autowired
    TitleRequestMapper titleRequestMapper

    def "toTitle maps all fields from TitleCreateRequest"() {
        given:
        def purchaseDetails = new PurchaseDetailsRequest()
        purchaseDetails.setStore("Amazon")
        purchaseDetails.setPrice(199.99f)
        purchaseDetails.setCurrency(MonetaryType.BRAZILIAN_REAL)

        def request = new TitleCreateRequest()
        request.setName("Princess Mononoke")
        request.setOriginalName("Mononoke-hime")
        request.setStudio("Studio Ghibli")
        request.setType(TitleType.BLU_RAY)
        request.setBarcode("123456789")
        request.setPurchaseDetails(purchaseDetails)

        when:
        def title = titleRequestMapper.toTitle(request)

        then:
        title.getName() == "Princess Mononoke"
        title.getOriginalName() == "Mononoke-hime"
        title.getStudio() == "Studio Ghibli"
        title.getType() == TitleType.BLU_RAY
        title.getBarcode() == "123456789"
        title.getPurchaseDetails() != null
        title.getPurchaseDetails().getStore() == "Amazon"
        Math.abs(title.getPurchaseDetails().getPrice().doubleValue() - 199.99d) < 0.01
        title.getPurchaseDetails().getCurrency() == MonetaryType.BRAZILIAN_REAL
    }

    def "toTitle maps minimal required fields"() {
        given:
        def request = new TitleCreateRequest()
        request.setName("Test Title")
        request.setType(TitleType.DVD)

        when:
        def title = titleRequestMapper.toTitle(request)

        then:
        title.getName() == "Test Title"
        title.getType() == TitleType.DVD
        title.getOriginalName() == null
        title.getStudio() == null
        title.getBarcode() == null
        title.getPurchaseDetails() == null
    }

    def "toTitle handles null purchaseDetails"() {
        given:
        def request = new TitleCreateRequest()
        request.setName("No Purchase")
        request.setType(TitleType.DVD)
        request.setPurchaseDetails(null)

        when:
        def title = titleRequestMapper.toTitle(request)

        then:
        title.getPurchaseDetails() == null
    }
}
