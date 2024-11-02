package br.edu.uaifood.ports.inbound.api

import br.edu.uaifood.adapters.OrderRepository
import br.edu.uaifood.domain.entities.OrderStatus.READY
import br.edu.uaifood.domain.entities.OrderStatus.RECEIVED
import br.edu.uaifood.ports.outbound.repository.order.OrderPersisted
import br.edu.uaifood.util.JsonReader
import com.ninjasquad.springmockk.MockkBean
import io.github.glytching.junit.extension.random.RandomBeansExtension
import io.mockk.every
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.ArrayList

@AutoConfigureMockMvc
@SpringBootTest
@ExtendWith(RandomBeansExtension::class)
class OrderControllerTest(
    @Autowired
    private val jsonReader: JsonReader,
    @Autowired
    private val mockMvc: MockMvc
) {
    @MockkBean
    private lateinit var orderRepository: OrderRepository

    @Test
    fun `should return order list`() {
        // Given
        val orders = ArrayList<OrderPersisted>()
        orders.add(OrderPersisted(1, RECEIVED))
        orders.add(OrderPersisted(2, READY))

        // When
        every { orderRepository.findAll() } returns orders

        mockMvc.perform(
            get("/v1/orders")

                .contentType(MediaType.APPLICATION_JSON)
        )
            // Then
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[0].status").value("RECEIVED"))
            .andExpect(jsonPath("$.[1].status").value("READY"))
    }
}