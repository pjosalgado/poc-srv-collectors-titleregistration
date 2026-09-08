package dev.pjosalgado.pocs.collectors.titleregistration.entrypoint.rest.mapper

import dev.pjosalgado.pocs.collectors.openapi.model.MonetaryType
import dev.pjosalgado.pocs.collectors.openapi.model.PurchaseDetailsRequest
import dev.pjosalgado.pocs.collectors.openapi.model.TitleCreateRequest
import dev.pjosalgado.pocs.collectors.openapi.model.MediaType
import dev.pjosalgado.pocs.collectors.openapi.model.TitleKind
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
        request.setMediaFormat(MediaType.BLU_RAY)
        request.setTitleCategory(TitleKind.MOVIE)
        request.setBarcode("123456789")
        request.setPurchaseDetails(purchaseDetails)

        when:
        def title = titleRequestMapper.toTitle(request)

        then:
        title.getName() == "Princess Mononoke"
        title.getOriginalName() == "Mononoke-hime"
        title.getStudio() == "Studio Ghibli"
        title.getMediaFormat() == MediaType.BLU_RAY
        title.getTitleCategory() == TitleKind.MOVIE
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
        request.setMediaFormat(MediaType.DVD)
        request.setTitleCategory(TitleKind.MOVIE)

        when:
        def title = titleRequestMapper.toTitle(request)

        then:
        title.getName() == "Test Title"
        title.getMediaFormat() == MediaType.DVD
        title.getTitleCategory() == TitleKind.MOVIE
        title.getOriginalName() == null
        title.getStudio() == null
        title.getBarcode() == null
        title.getPurchaseDetails() == null
    }

    def "toTitle handles null purchaseDetails"() {
        given:
        def request = new TitleCreateRequest()
        request.setName("No Purchase")
        request.setMediaFormat(MediaType.DVD)
        request.setTitleCategory(TitleKind.MOVIE)
        request.setPurchaseDetails(null)

        when:
        def title = titleRequestMapper.toTitle(request)

        then:
        title.getPurchaseDetails() == null
    }
}
