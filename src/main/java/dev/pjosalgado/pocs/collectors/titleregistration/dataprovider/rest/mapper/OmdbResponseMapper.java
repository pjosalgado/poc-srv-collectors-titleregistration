package dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.rest.mapper;

import dev.pjosalgado.pocs.collectors.titleregistration.core.model.Imdb;
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.RottenTomatoes;
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.TitleEnrichmentData;
import dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.rest.dto.OmdbResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OmdbResponseMapper {

    @Mapping(target = "imdb", expression = "java(mapImdb(response))")
    @Mapping(target = "rottenTomatoes", expression = "java(mapRottenTomatoes(response))")
    @Mapping(target = "plot", source = "plot")
    @Mapping(target = "posterUrl", source = "poster")
    @Mapping(target = "genre", source = "genre")
    @Mapping(target = "director", source = "director")
    @Mapping(target = "year", expression = "java(parseYear(response.getYear()))")
    @Mapping(target = "enrichedAt", expression = "java(java.time.LocalDateTime.now())")
    TitleEnrichmentData toTitleEnrichmentData(OmdbResponse response);

    default Imdb mapImdb(OmdbResponse response) {
        if (response == null) return null;
        return Imdb.builder()
                .rating(parseNumericValue(response.getImdbRating()))
                .build();
    }

    default RottenTomatoes mapRottenTomatoes(OmdbResponse response) {
        if (response == null || response.getRatings() == null) return null;

        return response.getRatings().stream()
                .filter(r -> "Rotten Tomatoes".equals(r.getSource()))
                .findFirst()
                .map(r -> {
                    Integer tomatometer = parseIntValue(r.getValue());
                    return RottenTomatoes.builder()
                            .tomatometer(tomatometer)
                            .criticRating(toCriticRating(tomatometer))
                            .build();
                })
                .orElse(null);
    }

    default Integer parseYear(String year) {
        if (year == null || year.isEmpty()) return null;
        try {
            java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("\\d{4}").matcher(year);
            if (matcher.find()) {
                return Integer.parseInt(matcher.group());
            }
            return null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    default Double parseNumericValue(String value) {
        if (value == null || "N/A".equals(value)) return null;
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    default Integer parseIntValue(String value) {
        if (value == null || "N/A".equals(value)) return null;
        try {
            return Integer.parseInt(value.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    default String toCriticRating(Integer score) {
        if (score == null) return null;
        if (score >= 60) return "fresh";
        return "rotten";
    }

}
