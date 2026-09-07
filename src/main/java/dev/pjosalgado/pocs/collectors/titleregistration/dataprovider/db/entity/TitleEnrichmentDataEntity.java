package dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.db.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode
@Builder
public class TitleEnrichmentDataEntity {

    private ImdbEntity imdb;
    private RottenTomatoesEntity rottenTomatoes;
    private String plot;
    private String posterUrl;
    private String genre;
    private String director;
    private Integer year;
    private LocalDateTime enrichedAt;

}
