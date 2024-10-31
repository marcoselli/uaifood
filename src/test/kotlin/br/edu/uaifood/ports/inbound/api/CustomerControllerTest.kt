package br.edu.uaifood.ports.inbound.api
import br.edu.uaifood.util.JsonReader
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc

import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@AutoConfigureMockMvc
@SpringBootTest
class CustomerControllerTest(
    @Autowired
    private val jsonReader: JsonReader,
    @Autowired
    private val mockMvc: MockMvc
) {

    @Test
    fun `should save a customer successfully`() {
        // Given
        val customerRequest = jsonReader.import("customer_request_ok.json")
        // When
        mockMvc.perform(
            post("/v1/customers")
                .content(customerRequest)
                .contentType(MediaType.APPLICATION_JSON)
        )
            // Then
            .andExpect(status().isCreated)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Name Surname"))
            .andExpect(jsonPath("$.cpf").value("91093363037"))
            .andExpect(jsonPath("$.e-mail").value("name.surname@gmail.com"))
            .andExpect(jsonPath("$.status").value("ACTIVE"))
    }

    @Test
    fun `should return bad request when cpf is invalid`() {
        // Given
        val customerRequest = jsonReader.import("customer_request_invalid_cpf.json")
        // When
        mockMvc.perform(
            post("/v1/customers")
                .content(customerRequest)
                .contentType(MediaType.APPLICATION_JSON)
        )
            // Then
            .andExpect(status().isBadRequest)
            .andExpect(content().string("400 BAD_REQUEST \"Invalid CPF\""))
    }

    @Test
    fun `should return bad request when e-mail is invalid`() {
        // Given
        val customerRequest = jsonReader.import("customer_request_invalid_email.json")
        // When
        mockMvc.perform(
            post("/v1/customers")
                .content(customerRequest)
                .contentType(MediaType.APPLICATION_JSON)
        )
            // Then
            .andExpect(status().isBadRequest)
            .andExpect(content().string("400 BAD_REQUEST \"Invalid e-mail\""))
    }

}