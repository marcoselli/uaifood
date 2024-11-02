package br.edu.uaifood.ports.inbound.api

import br.edu.uaifood.adapters.ProductRepository
import br.edu.uaifood.domain.entities.Product
import br.edu.uaifood.ports.inbound.api.product.dto.UpsertProductRequest
import br.edu.uaifood.ports.outbound.repository.product.ProductPersisted
import br.edu.uaifood.util.JsonReader
import com.ninjasquad.springmockk.MockkBean
import io.github.glytching.junit.extension.random.Random
import io.github.glytching.junit.extension.random.RandomBeansExtension
import io.mockk.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@AutoConfigureMockMvc
@SpringBootTest
@ExtendWith(RandomBeansExtension::class)
class ProductControllerTest(
    @Autowired
    private val jsonReader: JsonReader,
    @Autowired
    private val mockMvc: MockMvc
) {
    @MockkBean
    private lateinit var productRepository: ProductRepository

    @Test
    fun `should insert product into menu successfully`() {
        // Given
        val productRequest = jsonReader.import("product_request_ok.json")
        val productPersisted = ProductPersisted.from(
            jsonReader.importClass("product_request_ok.json", Product::class.java)
        )
        // When
        every { productRepository.findByName(any()) } returns null
        every { productRepository.save(any()) } returns productPersisted
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

    @Test
    fun `should update product from menu successfully`(@Random oldProduct: ProductPersisted) {
        // Given
        val productRequest = jsonReader.import("product_request_ok.json")
        // When
        every { productRepository.findByName(any()) } returns oldProduct.copy(name = "Coke")
        val product = jsonReader.importClass("product_request_ok.json", Product::class.java)
        val newProductPersisted = ProductPersisted.from(product).copy(id = oldProduct.id)
        every { productRepository.save(any()) } returns newProductPersisted
        mockMvc.perform(
            put("/v1/products/Coke")
                .content(productRequest)
                .contentType(MediaType.APPLICATION_JSON)
        )
            // Then
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.name").value("Coke"))
            .andExpect(jsonPath("$.description").value("Coca-cola 350ml"))
            .andExpect(jsonPath("$.price").value(10.00))
            .andExpect(jsonPath("$.category").value("DRINK"))
            .andExpect(jsonPath("$.image_url").value("base-img-url.com"))
    }

    @Test
    fun `should throw an error when update and query param and request body names are different`(
        @Random oldProduct: ProductPersisted
    ) {
        // Given
        val productRequest = jsonReader.import("product_request_ok.json")
        // When
        mockMvc.perform(
            put("/v1/products/ANY_OTHER_NAME_HERE")
                .content(productRequest)
                .contentType(MediaType.APPLICATION_JSON)
        )
            // Then
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.status_code").value(400))
            .andExpect(jsonPath("$.message").value("Path parameter and request body names must be equal"))
    }

    @Test
    fun `should remove product from menu successfully`(@Random productPersisted: ProductPersisted) {
        // Given
        every { productRepository.findByName(any()) } returns productPersisted
        every { productRepository.deleteById(any()) } just Runs
        // When
        mockMvc.perform(delete("/v1/products/ANY_PRODUCT_NAME_HERE"))
        // Then
            .andExpect(status().isNoContent)
    }

}