package dev.pjosalgado.pocs.collectors.titleregistration.config

import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset

class DateMappingUtilSpec extends Specification {

    @Unroll
    def "toOffsetDateTime converts #input to UTC offset"() {
        expect:
        DateMappingUtil.toOffsetDateTime(input) == expected

        where:
        input                                    || expected
        LocalDateTime.of(2025, 1, 15, 10, 30)   || OffsetDateTime.of(2025, 1, 15, 10, 30, 0, 0, ZoneOffset.UTC)
        LocalDateTime.of(2025, 12, 31, 23, 59)  || OffsetDateTime.of(2025, 12, 31, 23, 59, 0, 0, ZoneOffset.UTC)
        LocalDateTime.of(2025, 6, 15, 0, 0)     || OffsetDateTime.of(2025, 6, 15, 0, 0, 0, 0, ZoneOffset.UTC)
    }

    def "toOffsetDateTime returns null for null input"() {
        expect:
        DateMappingUtil.toOffsetDateTime(null) == null
    }
}
