package dev.pjosalgado.pocs.collectors.titleregistration.entrypoint.rest.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@UtilityClass
public class DateMappingUtils {

    public static OffsetDateTime toOffsetDateTime(LocalDateTime localDateTime) {
        return localDateTime == null
                ? null
                : localDateTime.atOffset(ZoneOffset.UTC);
    }

}
