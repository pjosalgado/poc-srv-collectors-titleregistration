package dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.cache.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@Builder
@AllArgsConstructor
public class ImdbCacheEntity {

    private Double rating;

}
