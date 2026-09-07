package dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.rest.mapper

import dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.rest.dto.OmdbResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class OmdbResponseMapperSpec extends Specification {

    @Autowired
    OmdbResponseMapper omdbResponseMapper

    def "toTitleEnrichmentData maps all fields from OmdbResponse"() {
        given:
        def ratings = [
                new OmdbResponse.Rating(source: "Internet Movie Database", value: "8.3/10"),
                new OmdbResponse.Rating(source: "Rotten Tomatoes", value: "93%"),
                new OmdbResponse.Rating(source: "Metacritic", value: "78/100")
        ]
        def response = OmdbResponse.builder()
                .imdbId("tt0119698")
                .title("Princess Mononoke")
                .year("1997")
                .genre("Animation, Adventure, Fantasy")
                .director("Hayao Miyazaki")
                .plot("A young boy encounters a befriended wolf.")
                .poster("https://m.media-amazon.com/image.jpg")
                .imdbRating("8.3")
                .imdbVotes("123456")
                .ratings(ratings)
                .response("True")
                .build()

        when:
        def result = omdbResponseMapper.toTitleEnrichmentData(response)

        then:
        result != null
        result.getPlot() == "A young boy encounters a befriended wolf."
        result.getPosterUrl() == "https://m.media-amazon.com/image.jpg"
        result.getGenre() == "Animation, Adventure, Fantasy"
        result.getDirector() == "Hayao Miyazaki"
        result.getYear() == 1997
        result.getEnrichedAt() != null
        result.getImdb() != null
        result.getImdb().getRating() == 8.3d
        result.getRottenTomatoes() != null
        result.getRottenTomatoes().getTomatometer() == 93
        result.getRottenTomatoes().getCriticRating() == "fresh"
    }

    def "mapImdb returns rating from response"() {
        given:
        def response = OmdbResponse.builder()
                .imdbRating("7.5")
                .build()

        when:
        def result = omdbResponseMapper.mapImdb(response)

        then:
        result != null
        result.getRating() == 7.5d
    }

    def "mapImdb returns null for N/A rating"() {
        given:
        def response = OmdbResponse.builder()
                .imdbRating("N/A")
                .build()

        when:
        def result = omdbResponseMapper.mapImdb(response)

        then:
        result != null
        result.getRating() == null
    }

    def "mapImdb returns null for null response"() {
        expect:
        omdbResponseMapper.mapImdb(null) == null
    }

    def "mapRottenTomatoes extracts RT rating from ratings list"() {
        given:
        def ratings = [
                new OmdbResponse.Rating(source: "Internet Movie Database", value: "8.3/10"),
                new OmdbResponse.Rating(source: "Rotten Tomatoes", value: "93%")
        ]
        def response = OmdbResponse.builder()
                .ratings(ratings)
                .build()

        when:
        def result = omdbResponseMapper.mapRottenTomatoes(response)

        then:
        result != null
        result.getTomatometer() == 93
        result.getCriticRating() == "fresh"
    }

    def "mapRottenTomatoes returns rotten for low score"() {
        given:
        def ratings = [
                new OmdbResponse.Rating(source: "Rotten Tomatoes", value: "45%")
        ]
        def response = OmdbResponse.builder()
                .ratings(ratings)
                .build()

        when:
        def result = omdbResponseMapper.mapRottenTomatoes(response)

        then:
        result != null
        result.getTomatometer() == 45
        result.getCriticRating() == "rotten"
    }

    def "mapRottenTomatoes returns null when no RT rating exists"() {
        given:
        def ratings = [
                new OmdbResponse.Rating(source: "Internet Movie Database", value: "8.3/10")
        ]
        def response = OmdbResponse.builder()
                .ratings(ratings)
                .build()

        when:
        def result = omdbResponseMapper.mapRottenTomatoes(response)

        then:
        result == null
    }

    def "mapRottenTomatoes returns null for null ratings"() {
        given:
        def response = OmdbResponse.builder()
                .ratings(null)
                .build()

        when:
        def result = omdbResponseMapper.mapRottenTomatoes(response)

        then:
        result == null
    }

    def "mapRottenTomatoes returns null for null response"() {
        expect:
        omdbResponseMapper.mapRottenTomatoes(null) == null
    }

    def "parseYear extracts year from various formats"() {
        expect:
        omdbResponseMapper.parseYear("1997") == 1997
        omdbResponseMapper.parseYear("2020–2023") == 2020
        omdbResponseMapper.parseYear("(1997)") == 1997
        omdbResponseMapper.parseYear(null) == null
        omdbResponseMapper.parseYear("") == null
    }

    def "parseNumericValue parses decimal strings"() {
        expect:
        omdbResponseMapper.parseNumericValue("8.3") == 8.3d
        omdbResponseMapper.parseNumericValue("N/A") == null
        omdbResponseMapper.parseNumericValue(null) == null
    }

    def "parseIntValue parses integer strings"() {
        expect:
        omdbResponseMapper.parseIntValue("93%") == 93
        omdbResponseMapper.parseIntValue("123456") == 123456
        omdbResponseMapper.parseIntValue("N/A") == null
        omdbResponseMapper.parseIntValue(null) == null
    }

    def "toCriticRating maps score to fresh or rotten"() {
        expect:
        omdbResponseMapper.toCriticRating(93) == "fresh"
        omdbResponseMapper.toCriticRating(60) == "fresh"
        omdbResponseMapper.toCriticRating(59) == "rotten"
        omdbResponseMapper.toCriticRating(0) == "rotten"
        omdbResponseMapper.toCriticRating(null) == null
    }
}
