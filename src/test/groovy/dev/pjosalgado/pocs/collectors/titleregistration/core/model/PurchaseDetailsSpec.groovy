package dev.pjosalgado.pocs.collectors.titleregistration.core.model

import dev.pjosalgado.pocs.collectors.openapi.model.MonetaryType
import spock.lang.Specification

class PurchaseDetailsSpec extends Specification {

    def "builder creates PurchaseDetails with all fields"() {
        when:
        def details = PurchaseDetails.builder()
                .store("Amazon")
                .price(new BigDecimal("199.99"))
                .currency(MonetaryType.BRAZILIAN_REAL)
                .build()

        then:
        details.getStore() == "Amazon"
        details.getPrice() == new BigDecimal("199.99")
        details.getCurrency() == MonetaryType.BRAZILIAN_REAL
    }

    def "builder creates PurchaseDetails with null fields"() {
        when:
        def details = PurchaseDetails.builder().build()

        then:
        details.getStore() == null
        details.getPrice() == null
        details.getCurrency() == null
    }

    def "equals and hashCode work correctly"() {
        given:
        def details1 = PurchaseDetails.builder()
                .store("Amazon")
                .price(new BigDecimal("99.99"))
                .currency(MonetaryType.DOLLAR)
                .build()
        def details2 = PurchaseDetails.builder()
                .store("Amazon")
                .price(new BigDecimal("99.99"))
                .currency(MonetaryType.DOLLAR)
                .build()
        def details3 = PurchaseDetails.builder()
                .store("Ebay")
                .price(new BigDecimal("99.99"))
                .currency(MonetaryType.DOLLAR)
                .build()

        expect:
        details1 == details2
        details1 != details3
        details1.hashCode() == details2.hashCode()
    }

    def "toString includes all fields"() {
        given:
        def details = PurchaseDetails.builder()
                .store("Amazon")
                .price(new BigDecimal("99.99"))
                .currency(MonetaryType.DOLLAR)
                .build()

        when:
        def str = details.toString()

        then:
        str.contains("Amazon")
        str.contains("99.99")
        str.contains("DOLLAR")
    }
}
