package dev.pjosalgado.pocs.collectors.titleregistration.core.model

import dev.pjosalgado.pocs.collectors.openapi.model.MonetaryType
import dev.pjosalgado.pocs.collectors.openapi.model.MediaType
import dev.pjosalgado.pocs.collectors.openapi.model.TitleKind
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
                .mediaFormat(MediaType.DVD)
                .titleCategory(TitleKind.MOVIE)
                .build()

        when:
        def updates = Title.builder()
                .name("New Name")
                .mediaFormat(MediaType.BLU_RAY)
                .build()
        title.applyUpdates(updates)

        then:
        title.getName() == "New Name"
        title.getStudio() == "Original Studio"
        title.getMediaFormat() == MediaType.BLU_RAY
        title.getTitleCategory() == TitleKind.MOVIE
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
                .mediaFormat(MediaType.DVD)
                .titleCategory(TitleKind.TV_SHOW)
                .barcode("123")
                .purchaseDetails(purchaseDetails)
                .build()

        then:
        title.getTitleId() == "id-1"
        title.getName() == "Name"
        title.getOriginalName() == "Original"
        title.getStudio() == "Studio"
        title.getMediaFormat() == MediaType.DVD
        title.getTitleCategory() == TitleKind.TV_SHOW
        title.getBarcode() == "123"
        title.getPurchaseDetails() == purchaseDetails
    }
}
