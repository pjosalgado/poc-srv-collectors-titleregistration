package dev.pjosalgado.pocs.collectors.titleregistration.core.model

import dev.pjosalgado.pocs.collectors.openapi.model.MonetaryType
import dev.pjosalgado.pocs.collectors.openapi.model.TitleType
import spock.lang.Specification

class TitleSpec extends Specification {

    def "generateId sets a UUID"() {
        given:
        def title = Title.builder().build()

        when:
        title.generateId()

        then:
        title.getTitleId() != null
        title.getTitleId().matches('[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}')
    }

    def "applyUpdates only updates non-null fields"() {
        given:
        def title = Title.builder()
                .titleId("id-1")
                .name("Original Name")
                .studio("Original Studio")
                .type(TitleType.DVD)
                .build()

        when:
        def updates = Title.builder()
                .name("New Name")
                .type(TitleType.BLU_RAY)
                .build()
        title.applyUpdates(updates)

        then:
        title.getName() == "New Name"
        title.getStudio() == "Original Studio"
        title.getType() == TitleType.BLU_RAY
        title.getTitleId() == "id-1"
    }

    def "builder creates a Title with all fields"() {
        given:
        def purchaseDetails = PurchaseDetails.builder()
                .store("Amazon")
                .price(new BigDecimal("99.99"))
                .currency(MonetaryType.BRAZILIAN_REAL)
                .build()

        when:
        def title = Title.builder()
                .titleId("id-1")
                .name("Name")
                .originalName("Original")
                .studio("Studio")
                .type(TitleType.DVD)
                .barcode("123")
                .purchaseDetails(purchaseDetails)
                .build()

        then:
        title.getTitleId() == "id-1"
        title.getName() == "Name"
        title.getOriginalName() == "Original"
        title.getStudio() == "Studio"
        title.getType() == TitleType.DVD
        title.getBarcode() == "123"
        title.getPurchaseDetails() == purchaseDetails
    }
}
