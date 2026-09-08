package dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.mapper

import dev.pjosalgado.pocs.collectors.openapi.model.MonetaryType
import dev.pjosalgado.pocs.collectors.openapi.model.MediaType
import dev.pjosalgado.pocs.collectors.openapi.model.TitleKind
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.PurchaseDetails
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.Title
import dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.db.entity.PurchaseDetailsEntity
import dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.db.entity.TitleEntity
import dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.db.mapper.TitleEntityMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import java.time.LocalDateTime

@SpringBootTest
class TitleEntityMapperSpec extends Specification {

    @Autowired
    TitleEntityMapper titleEntityMapper

    def "toEntity maps all fields from Title to TitleEntity"() {
        given:
        def purchaseDetails = PurchaseDetails.builder()
                .store("Amazon")
                .price(new BigDecimal("199.99"))
                .currency(MonetaryType.BRAZILIAN_REAL)
                .build()
        def title = Title.builder()
                .titleId("uuid-123")
                .name("Princess Mononoke")
                .originalName("Mononoke-hime")
                .studio("Studio Ghibli")
                .mediaFormat(MediaType.BLU_RAY)
                .titleCategory(TitleKind.MOVIE)
                .barcode("123456789")
                .purchaseDetails(purchaseDetails)
                .createdDateTime(LocalDateTime.of(2025, 1, 15, 10, 30))
                .build()

        when:
        def entity = titleEntityMapper.toEntity(title)

        then:
        entity.getTitleId() == "uuid-123"
        entity.getName() == "Princess Mononoke"
        entity.getOriginalName() == "Mononoke-hime"
        entity.getStudio() == "Studio Ghibli"
        entity.getMediaFormat() == MediaType.BLU_RAY
        entity.getTitleCategory() == TitleKind.MOVIE
        entity.getBarcode() == "123456789"
        entity.getCreatedDateTime() == LocalDateTime.of(2025, 1, 15, 10, 30)
        entity.getPurchaseDetails() != null
        entity.getPurchaseDetails().getStore() == "Amazon"
        entity.getPurchaseDetails().getPrice() == new BigDecimal("199.99")
        entity.getPurchaseDetails().getCurrency() == MonetaryType.BRAZILIAN_REAL
    }

    def "toTitle maps all fields from TitleEntity to Title"() {
        given:
        def purchaseDetailsEntity = PurchaseDetailsEntity.builder()
                .store("Ebay")
                .price(new BigDecimal("99.99"))
                .currency(MonetaryType.DOLLAR)
                .build()
        def entity = TitleEntity.builder()
                .titleId("uuid-456")
                .name("Spirited Away")
                .originalName("Sen to Chihiro")
                .studio("Studio Ghibli")
                .mediaFormat(MediaType.DVD)
                .titleCategory(TitleKind.MOVIE)
                .barcode("987654321")
                .purchaseDetails(purchaseDetailsEntity)
                .createdDateTime(LocalDateTime.of(2025, 6, 15, 14, 30))
                .build()

        when:
        def title = titleEntityMapper.toTitle(entity)

        then:
        title.getTitleId() == "uuid-456"
        title.getName() == "Spirited Away"
        title.getOriginalName() == "Sen to Chihiro"
        title.getStudio() == "Studio Ghibli"
        title.getMediaFormat() == MediaType.DVD
        title.getTitleCategory() == TitleKind.MOVIE
        title.getBarcode() == "987654321"
        title.getCreatedDateTime() == LocalDateTime.of(2025, 6, 15, 14, 30)
        title.getPurchaseDetails() != null
        title.getPurchaseDetails().getStore() == "Ebay"
        title.getPurchaseDetails().getPrice() == new BigDecimal("99.99")
        title.getPurchaseDetails().getCurrency() == MonetaryType.DOLLAR
    }

    def "toEntity handles null purchaseDetails"() {
        given:
        def title = Title.builder()
                .titleId("uuid-789")
                .name("No Purchase")
                .mediaFormat(MediaType.DVD)
                .build()

        when:
        def entity = titleEntityMapper.toEntity(title)

        then:
        entity.getPurchaseDetails() == null
    }

    def "toTitle handles null purchaseDetails"() {
        given:
        def entity = TitleEntity.builder()
                .titleId("uuid-012")
                .name("No Purchase")
                .mediaFormat(MediaType.DVD)
                .build()

        when:
        def title = titleEntityMapper.toTitle(entity)

        then:
        title.getPurchaseDetails() == null
    }

    def "roundtrip: toEntity then toTitle preserves all fields"() {
        given:
        def purchaseDetails = PurchaseDetails.builder()
                .store("Target")
                .price(new BigDecimal("29.99"))
                .currency(MonetaryType.DOLLAR)
                .build()
        def original = Title.builder()
                .titleId("uuid-roundtrip")
                .name("Roundtrip Test")
                .originalName("Original")
                .studio("Test Studio")
                .mediaFormat(MediaType.COMBO)
                .titleCategory(TitleKind.DOCUMENTARY)
                .barcode("barcode-rt")
                .purchaseDetails(purchaseDetails)
                .createdDateTime(LocalDateTime.of(2025, 3, 20, 8, 0))
                .build()

        when:
        def entity = titleEntityMapper.toEntity(original)
        def restored = titleEntityMapper.toTitle(entity)

        then:
        restored.getTitleId() == original.getTitleId()
        restored.getName() == original.getName()
        restored.getOriginalName() == original.getOriginalName()
        restored.getStudio() == original.getStudio()
        restored.getMediaFormat() == original.getMediaFormat()
        restored.getTitleCategory() == original.getTitleCategory()
        restored.getBarcode() == original.getBarcode()
        restored.getCreatedDateTime() == original.getCreatedDateTime()
        restored.getPurchaseDetails().getStore() == original.getPurchaseDetails().getStore()
        restored.getPurchaseDetails().getPrice() == original.getPurchaseDetails().getPrice()
        restored.getPurchaseDetails().getCurrency() == original.getPurchaseDetails().getCurrency()
    }
}
