package br.edu.uaifood.ports.inbound.api

import br.edu.uaifood.adapters.OrderRepository
import br.edu.uaifood.domain.entities.OrderStatus.*
import br.edu.uaifood.ports.outbound.repository.order.OrderPersisted
import br.edu.uaifood.ports.outbound.repository.product.ProductPersisted
import com.ninjasquad.springmockk.MockkBean
import io.github.glytching.junit.extension.random.Random
import io.github.glytching.junit.extension.random.RandomBeansExtension
import io.mockk.every
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDateTime

@AutoConfigureMockMvc
@SpringBootTest
@ExtendWith(RandomBeansExtension::class)
class OrderControllerTest(
    @Autowired
    private val mockMvc: MockMvc
) {
    @MockkBean
    private lateinit var orderRepository: OrderRepository

    @Test
    fun `should find all orders`(@Random randomProduct: ProductPersisted) {
        // Given
        val firstOrder = OrderPersisted(1, listOf(randomProduct), READY, LocalDateTime.parse("2023-12-23T19:34:50.63"))
        val secondOrder = OrderPersisted(2, listOf(randomProduct), FINISHED,  LocalDateTime.parse("2023-06-20T07:12:10.02"))

        // When
        every { orderRepository.findAll() } returns listOf(firstOrder, secondOrder)

        mockMvc.perform(
            get("/v1/orders")
        )
            // Then
            .andExpect(status().isOk)
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$.[0].status").value("READY"))
            .andExpect(jsonPath("$.[0].creation_date").value("2023-12-23T19:34:50.630"))
            .andExpect(jsonPath("$.[0].products[0].name").value(randomProduct.name))
            .andExpect(jsonPath("$.[1].status").value("FINISHED"))
            .andExpect(jsonPath("$.[1].creation_date").value("2023-06-20T07:12:10.020"))
            .andExpect(jsonPath("$.[1].products[0].name").value(randomProduct.name))
    }
}