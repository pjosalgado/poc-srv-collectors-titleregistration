package dev.pjosalgado.pocs.collectors.titleregistration.component

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
class TitleComponentSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    def "full CRUD lifecycle"() {
        when: "create a title"
        def createResult = mockMvc.perform(post("/registration/v1/titles")
                .contentType(MediaType.APPLICATION_JSON)
                .content('{"data":{"name":"Spirited Away","studio":"Studio Ghibli","type":"BLU_RAY_4K","purchaseDetails":{"store":"Amazon","price":199.99,"currency":"BRAZILIAN_REAL"}}}'))
        def createdBody = objectMapper.readTree(createResult.andReturn().response.contentAsString)
        def titleId = createdBody.get("data").get("titleId").asText()

        then:
        createResult.andExpect(status().isCreated())
        createdBody.get("data").get("name").asText() == "Spirited Away"
        createdBody.get("data").get("purchaseDetails").get("store").asText() == "Amazon"

        when: "find by id"
        def findResult = mockMvc.perform(get("/registration/v1/titles/${titleId}"))
        def foundBody = objectMapper.readTree(findResult.andReturn().response.contentAsString)

        then:
        findResult.andExpect(status().isOk())
        foundBody.get("data").get("titleId").asText() == titleId
        foundBody.get("data").get("name").asText() == "Spirited Away"

        when: "update name"
        def updateResult = mockMvc.perform(patch("/registration/v1/titles/${titleId}")
                .contentType(MediaType.APPLICATION_JSON)
                .content('{"data":{"name":"My Neighbor Totoro"}}'))
        def updatedBody = objectMapper.readTree(updateResult.andReturn().response.contentAsString)

        then:
        updateResult.andExpect(status().isOk())
        updatedBody.get("data").get("name").asText() == "My Neighbor Totoro"
        updatedBody.get("data").get("studio").asText() == "Studio Ghibli"

        when: "delete"
        def deleteResult = mockMvc.perform(delete("/registration/v1/titles/${titleId}"))

        then:
        deleteResult.andExpect(status().isNoContent())

        when: "verify deleted"
        mockMvc.perform(get("/registration/v1/titles/${titleId}"))

        then:
        mockMvc.perform(get("/registration/v1/titles/${titleId}"))
                .andExpect(status().isNotFound())
    }

    def "create with missing required fields returns 400"() {
        expect:
        mockMvc.perform(post("/registration/v1/titles")
                .contentType(MediaType.APPLICATION_JSON)
                .content('{"data":{"studio":"Studio Ghibli"}}'))
                .andExpect(status().isBadRequest())
    }
}
