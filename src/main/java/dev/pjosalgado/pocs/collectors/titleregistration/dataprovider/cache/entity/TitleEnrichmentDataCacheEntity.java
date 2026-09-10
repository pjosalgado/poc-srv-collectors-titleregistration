package dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.cache.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode
@Builder
@AllArgsConstructor
public class TitleEnrichmentDataCacheEntity {

    private ImdbCacheEntity imdb;
    private RottenTomatoesCacheEntity rottenTomatoes;
    private String plot;
    private String posterUrl;
    private String genre;
    private String director;
    private Integer year;
    private LocalDateTime enrichedAt;

}
