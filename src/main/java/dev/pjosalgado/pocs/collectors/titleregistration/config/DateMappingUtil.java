package dev.pjosalgado.pocs.collectors.titleregistration.config;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@UtilityClass
public class DateMappingUtil {

    public static OffsetDateTime toOffsetDateTime(LocalDateTime localDateTime) {
        return localDateTime == null
                ? null
                : localDateTime.atOffset(ZoneOffset.UTC);
    }

}
