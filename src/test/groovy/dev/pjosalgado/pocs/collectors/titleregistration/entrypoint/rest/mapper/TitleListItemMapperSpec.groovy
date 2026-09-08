package dev.pjosalgado.pocs.collectors.titleregistration.entrypoint.rest.mapper

import dev.pjosalgado.pocs.collectors.openapi.model.MediaType
import dev.pjosalgado.pocs.collectors.openapi.model.TitleKind
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.Imdb
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.RottenTomatoes
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.Title
import dev.pjosalgado.pocs.collectors.titleregistration.core.model.TitleEnrichmentData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import java.time.LocalDateTime

@SpringBootTest
class TitleListItemMapperSpec extends Specification {

    @Autowired
    TitleListItemMapper titleListItemMapper

    def "fromTitle maps all fields including enrichmentData"() {
        given:
        def enrichmentData = TitleEnrichmentData.builder()
                .posterUrl("https://m.media-amazon.com/images/M/MV5BMjlmZmI5MDctNDE2YS00YWE0LWE5ZWItZDBhYWQ0NTcxNWRhXkEyXkFqcGdeQXVyMTMxODk2OTU@.jpg")
                .genre("Animation, Adventure, Fantasy")
                .director("Hayao Miyazaki")
                .year(1997)
                .plot("A young boy encounters a befriended wolf.")
                .enrichedAt(LocalDateTime.of(2025, 6, 15, 14, 30, 0))
                .imdb(Imdb.builder().rating(8.3).build())
                .rottenTomatoes(RottenTomatoes.builder().tomatometer(93).criticRating("fresh").build())
                .build()
        def title = Title.builder()
                .titleId("0dc42d37-eb5c-4287-bc51-128260d0ef25")
                .name("Princess Mononoke")
                .originalName("Mononoke-hime")
                .studio("Studio Ghibli")
                .mediaFormat(MediaType.BLU_RAY)
                .titleCategory(TitleKind.MOVIE)
                .enrichmentData(enrichmentData)
                .build()

        when:
        def response = titleListItemMapper.fromTitle(title)

        then:
        response.getTitleId().toString() == "0dc42d37-eb5c-4287-bc51-128260d0ef25"
        response.getName() == "Princess Mononoke"
        response.getOriginalName() == "Mononoke-hime"
        response.getStudio() == "Studio Ghibli"
        response.getMediaFormat() == MediaType.BLU_RAY
        response.getTitleCategory() == TitleKind.MOVIE
        response.getEnrichmentData() != null
        response.getEnrichmentData().getPosterUrl() != null
        response.getEnrichmentData().getPosterUrl().toString().startsWith("https://")
        response.getEnrichmentData().getGenre() == "Animation, Adventure, Fantasy"
        response.getEnrichmentData().getDirector() == "Hayao Miyazaki"
        response.getEnrichmentData().getImdb() != null
        response.getEnrichmentData().getImdb().getRating() == 8.3d
        response.getEnrichmentData().getRottenTomatoes() != null
        response.getEnrichmentData().getRottenTomatoes().getTomatometer() == 93
        response.getEnrichmentData().getRottenTomatoes().getCriticRating() == "fresh"
    }

    def "fromTitle maps without enrichmentData"() {
        given:
        def title = Title.builder()
                .titleId("1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d")
                .name("Test Title")
                .mediaFormat(MediaType.DVD)
                .titleCategory(TitleKind.TV_SHOW)
                .build()

        when:
        def response = titleListItemMapper.fromTitle(title)

        then:
        response.getTitleId().toString() == "1a2b3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d"
        response.getName() == "Test Title"
        response.getMediaFormat() == MediaType.DVD
        response.getTitleCategory() == TitleKind.TV_SHOW
        response.getEnrichmentData() == null
    }

    def "fromTitle converts null posterUrl to null URI"() {
        given:
        def enrichmentData = TitleEnrichmentData.builder()
                .posterUrl(null)
                .genre("Drama")
                .director("Test Director")
                .build()
        def title = Title.builder()
                .titleId("2b3c4d5e-6f7a-8b9c-0d1e-2f3a4b5c6d7e")
                .name("No Poster")
                .mediaFormat(MediaType.DVD)
                .titleCategory(TitleKind.MOVIE)
                .enrichmentData(enrichmentData)
                .build()

        when:
        def response = titleListItemMapper.fromTitle(title)

        then:
        response.getEnrichmentData() != null
        response.getEnrichmentData().getPosterUrl() == null
    }

    def "toUri converts valid URL string to URI"() {
        expect:
        def uri = titleListItemMapper.toUri("https://example.com/image.jpg")
        uri.toString() == "https://example.com/image.jpg"
    }

    def "toUri returns null for null input"() {
        expect:
        titleListItemMapper.toUri(null) == null
    }
}
