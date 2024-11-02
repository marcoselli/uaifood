package br.edu.uaifood.ports.inbound.api

import br.edu.uaifood.adapters.CheckoutService
import br.edu.uaifood.adapters.OrderRepository
import br.edu.uaifood.domain.entities.Order
import br.edu.uaifood.domain.entities.OrderStatus.*
import br.edu.uaifood.ports.inbound.api.order.dto.OrderRequest
import br.edu.uaifood.ports.outbound.repository.order.OrderPersisted
import br.edu.uaifood.ports.outbound.repository.product.ProductPersisted
import br.edu.uaifood.util.JsonReader
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDateTime

@AutoConfigureMockMvc
@SpringBootTest
@ExtendWith(RandomBeansExtension::class)
class OrderControllerTest(
    @Autowired
    private val mockMvc: MockMvc,
    @Autowired
private val jsonReader: JsonReader,
) {
    @MockkBean
    private lateinit var orderRepository: OrderRepository

    @MockkBean
    private lateinit var checkoutService: CheckoutService

            @Test
    fun `should find all orders`(@Random randomProduct: ProductPersisted) {
        // Given
        val firstOrder = OrderPersisted(1, listOf(randomProduct), READY, LocalDateTime.parse("2023-12-23T19:34:50.63"), null)
        val secondOrder = OrderPersisted(2, listOf(randomProduct), FINISHED,  LocalDateTime.parse("2023-06-20T07:12:10.02"), null)

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

    @Test
    fun `should save a order successfully`() {
        // Given
        val orderRequest = jsonReader.import("order_request_ok.json")
        val orderPersisted = OrderPersisted.from(
            Order.from(
                jsonReader.importClass("order_request_ok.json", OrderRequest::class.java),
                null
            )
        )

        // When
        every { orderRepository.save(any()) } returns orderPersisted
        every { checkoutService.fakeCheckout() } returns true

        mockMvc.perform(
            post("/v1/orders")
                .content(orderRequest)
                .contentType(APPLICATION_JSON)
        )
            // Then
            .andExpect(status().isCreated)
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$.status").value("RECEIVED"))
            .andExpect(jsonPath("$.products[0].name").value("Coke"))
            .andExpect(jsonPath("$.products[1].name").value("Pizza"))
    }

    @Test
    fun `should save a order with Cpf if customer choose to identify via Cpf`() {
        // Given
        val orderRequest = jsonReader.import("order_request_ok.json")
        val orderPersisted = OrderPersisted.from(
            Order.from(
                jsonReader.importClass("order_request_ok.json", OrderRequest::class.java),
                "910.933.630-37"
            )
        )

        // When
        every { orderRepository.save(any()) } returns orderPersisted
        every { checkoutService.fakeCheckout() } returns true

        mockMvc.perform(
            post("/v1/orders?cpf=910.933.630-37")
                .content(orderRequest)
                .contentType(APPLICATION_JSON)
        )
            // Then
            .andExpect(status().isCreated)
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$.status").value("RECEIVED"))
            .andExpect(jsonPath("$.products[0].name").value("Coke"))
            .andExpect(jsonPath("$.products[1].name").value("Pizza"))
    }


    @Test
    fun `should not save a order if payment is not confirmed`() {
        // Given
        val orderRequest = jsonReader.import("order_request_ok.json")

        // When
        every { checkoutService.fakeCheckout() } returns false
        mockMvc.perform(
            post("/v1/orders")
                .content(orderRequest)
                .contentType(APPLICATION_JSON)
        )
            // Then
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$.status_code").value(400))
            .andExpect(jsonPath("$.message").value("There was a problem with payment and the order was not received"))
    }
}