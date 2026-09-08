package dev.pjosalgado.pocs.collectors.titleregistration.dataprovider.db

import dev.pjosalgado.pocs.collectors.titleregistration.core.model.Title
import dev.pjosalgado.pocs.collectors.openapi.model.MediaType
import dev.pjosalgado.pocs.collectors.openapi.model.TitleKind
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification
import spock.lang.Ignore

@SpringBootTest
@TestPropertySource(properties = ["spring.mongodb.uri=mongodb://localhost:27017/titleregistration"])
@Ignore("Requires MongoDB running on localhost:27017")
class TitleGatewaySpec extends Specification {

    @Autowired
    TitlePersistenceGateway titleGateway

    def "create and findById"() {
        given:
        def title = Title.builder()
                .name("Princess Mononoke")
                .studio("Studio Ghibli")
                .mediaFormat(MediaType.BLU_RAY)
                .titleCategory(TitleKind.MOVIE)
                .build()

        when:
        def created = titleGateway.create(title)

        then:
        created.getTitleId() != null
        created.getName() == "Princess Mononoke"

        when:
        def found = titleGateway.findById(created.getTitleId())

        then:
        found.isPresent()
        found.get().getName() == "Princess Mononoke"
    }

    def "update modifies existing title"() {
        given:
        def created = titleGateway.create(
                Title.builder().name("Old Name").studio("Studio").mediaFormat(MediaType.DVD).titleCategory(TitleKind.MOVIE).build()
        )

        when:
        def updates = Title.builder().name("New Name").build()
        created.applyUpdates(updates)
        def updated = titleGateway.update(created)

        then:
        updated.getName() == "New Name"

        when:
        def found = titleGateway.findById(updated.getTitleId())

        then:
        found.get().getName() == "New Name"
    }

    def "deleteById removes title"() {
        given:
        def created = titleGateway.create(
                Title.builder().name("To Delete").mediaFormat(MediaType.DVD).titleCategory(TitleKind.MOVIE).build()
        )

        when:
        titleGateway.deleteById(created.getTitleId())

        then:
        titleGateway.findById(created.getTitleId()).isEmpty()
    }

    def "findById returns empty for nonexistent id"() {
        expect:
        titleGateway.findById("nonexistent").isEmpty()
    }
}
