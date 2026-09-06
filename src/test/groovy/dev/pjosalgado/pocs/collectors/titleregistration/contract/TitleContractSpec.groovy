package dev.pjosalgado.pocs.collectors.titleregistration.contract

import com.fasterxml.jackson.databind.ObjectMapper
import dev.pjosalgado.pocs.collectors.titleregistration.TitleRegistrationApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.lang.Ignore

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest(classes = TitleRegistrationApplication)
@AutoConfigureMockMvc
@TestPropertySource(properties = ["spring.mongodb.uri=mongodb://localhost:27017/titleregistration"])
@Ignore("Requires MongoDB running on localhost:27017")
class TitleContractSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    def "POST /registration/v1/titles returns 201 with valid contract"() {
        given:
        def request = """
        {
            "data": {
                "name": "Princess Mononoke",
                "studio": "Studio Ghibli",
                "type": "BLU_RAY"
            }
        }
        """

        when:
        def result = mockMvc.perform(post("/registration/v1/titles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))

        then:
        result.andExpect(status().isCreated())
        result.andExpect(jsonPath('$.data.titleId').isNotEmpty())
        result.andExpect(jsonPath('$.data.name').value("Princess Mononoke"))
        result.andExpect(jsonPath('$.data.studio').value("Studio Ghibli"))
        result.andExpect(jsonPath('$.data.type').value("BLU_RAY"))
        result.andExpect(jsonPath('$.data.createdDateTime').isNotEmpty())
    }

    def "GET /registration/v1/titles/{id} returns 200 with valid contract"() {
        given:
        def createResult = mockMvc.perform(post("/registration/v1/titles")
                .contentType(MediaType.APPLICATION_JSON)
                .content('{"data":{"name":"Test","type":"DVD"}}'))
        def body = objectMapper.readTree(createResult.andReturn().response.contentAsString)
        def titleId = body.get("data").get("titleId").asText()

        when:
        def result = mockMvc.perform(get("/registration/v1/titles/${titleId}"))

        then:
        result.andExpect(status().isOk())
        result.andExpect(jsonPath('$.data.titleId').value(titleId))
        result.andExpect(jsonPath('$.data.name').value("Test"))
    }

    def "GET /registration/v1/titles/{id} returns 404 for nonexistent"() {
        expect:
        mockMvc.perform(get("/registration/v1/titles/nonexistent"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath('$.code').value("NOT_FOUND"))
    }

    def "PATCH /registration/v1/titles/{id} returns 200 with valid contract"() {
        given:
        def createResult = mockMvc.perform(post("/registration/v1/titles")
                .contentType(MediaType.APPLICATION_JSON)
                .content('{"data":{"name":"Original","type":"DVD"}}'))
        def body = objectMapper.readTree(createResult.andReturn().response.contentAsString)
        def titleId = body.get("data").get("titleId").asText()

        when:
        def result = mockMvc.perform(patch("/registration/v1/titles/${titleId}")
                .contentType(MediaType.APPLICATION_JSON)
                .content('{"data":{"name":"Updated"}}'))

        then:
        result.andExpect(status().isOk())
        result.andExpect(jsonPath('$.data.titleId').value(titleId))
        result.andExpect(jsonPath('$.data.name').value("Updated"))
    }

    def "DELETE /registration/v1/titles/{id} returns 204"() {
        given:
        def createResult = mockMvc.perform(post("/registration/v1/titles")
                .contentType(MediaType.APPLICATION_JSON)
                .content('{"data":{"name":"To Delete","type":"DVD"}}'))
        def body = objectMapper.readTree(createResult.andReturn().response.contentAsString)
        def titleId = body.get("data").get("titleId").asText()

        expect:
        mockMvc.perform(delete("/registration/v1/titles/${titleId}"))
                .andExpect(status().isNoContent())
    }
}
