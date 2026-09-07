package dev.pjosalgado.pocs.collectors.titleregistration.core.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode
@Builder
public class TitleEnrichmentData {

    private Imdb imdb;
    private RottenTomatoes rottenTomatoes;
    private String plot;
    private String posterUrl;
    private String genre;
    private String director;
    private Integer year;
    private LocalDateTime enrichedAt;

}
