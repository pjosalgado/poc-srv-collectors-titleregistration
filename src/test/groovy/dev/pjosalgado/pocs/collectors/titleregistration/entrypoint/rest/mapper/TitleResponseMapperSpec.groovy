package dev.pjosalgado.pocs.collectors.titleregistration.entrypoint.rest.mapper

import dev.pjosalgado.pocs.collectors.openapi.model.MonetaryType
import dev.pjosalgado.pocs.collectors.openapi.model.TitleType
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.PurchaseDetails
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.Title
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import java.time.LocalDateTime

@SpringBootTest
class TitleResponseMapperSpec extends Specification {

    @Autowired
    TitleResponseMapper titleResponseMapper

    def "fromTitle maps all fields to TitleCreatedResponse"() {
        given:
        def purchaseDetails = PurchaseDetails.builder()
                .store("Amazon")
                .price(new BigDecimal("199.99"))
                .currency(MonetaryType.BRAZILIAN_REAL)
                .build()
        def title = Title.builder()
                .titleId(UUID.randomUUID().toString())
                .name("Princess Mononoke")
                .originalName("Mononoke-hime")
                .studio("Studio Ghibli")
                .type(TitleType.BLU_RAY)
                .barcode("123456789")
                .purchaseDetails(purchaseDetails)
                .createdDateTime(LocalDateTime.of(2025, 1, 15, 10, 30))
                .build()

        when:
        def response = titleResponseMapper.fromTitle(title)

        then:
        response.getTitleId().toString() == title.getTitleId()
        response.getName() == "Princess Mononoke"
        response.getOriginalName() == "Mononoke-hime"
        response.getStudio() == "Studio Ghibli"
        response.getType() == TitleType.BLU_RAY
        response.getBarcode() == "123456789"
        response.getCreatedDateTime() != null
        response.getPurchaseDetails() != null
        response.getPurchaseDetails().getStore() == "Amazon"
        response.getPurchaseDetails().getPrice() == 199.99f
        response.getPurchaseDetails().getCurrency() == MonetaryType.BRAZILIAN_REAL
    }

    def "fromTitle maps minimal fields"() {
        given:
        def title = Title.builder()
                .titleId(UUID.randomUUID().toString())
                .name("Test")
                .type(TitleType.DVD)
                .build()

        when:
        def response = titleResponseMapper.fromTitle(title)

        then:
        response.getTitleId().toString() == title.getTitleId()
        response.getName() == "Test"
        response.getType() == TitleType.DVD
        response.getOriginalName() == null
        response.getStudio() == null
        response.getBarcode() == null
        response.getPurchaseDetails() == null
        response.getCreatedDateTime() == null
    }

    def "fromTitle converts LocalDateTime to OffsetDateTime"() {
        given:
        def title = Title.builder()
                .titleId(UUID.randomUUID().toString())
                .name("Date Test")
                .type(TitleType.DVD)
                .createdDateTime(LocalDateTime.of(2025, 6, 15, 14, 30, 0))
                .build()

        when:
        def response = titleResponseMapper.fromTitle(title)

        then:
        response.getCreatedDateTime() != null
        response.getCreatedDateTime().getOffset() != null
    }
}
