package br.edu.uaifood.ports.inbound.api

import br.edu.uaifood.util.JsonReader
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@AutoConfigureMockMvc
@SpringBootTest
class ProductControllerTest(
    @Autowired
    private val jsonReader: JsonReader,
    @Autowired
    private val mockMvc: MockMvc
) {

    @Test
    fun `should insert product into menu successfully`() {
        // Given
        val productRequest = jsonReader.import("product_request_ok.json")
        // When
        mockMvc.perform(
            post("/v1/products")
                .content(productRequest)
                .contentType(MediaType.APPLICATION_JSON)
        )
        // Then
            .andExpect(status().isCreated)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.name").value("Coke"))
            .andExpect(jsonPath("$.description").value("Coca-cola 350ml"))
            .andExpect(jsonPath("$.price").value(10.00))
            .andExpect(jsonPath("$.category").value("DRINK"))
            .andExpect(jsonPath("$.image_url").value("base-img-url.com"))
        }
}