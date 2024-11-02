package br.edu.uaifood.domain.services

import br.edu.uaifood.adapters.OrderRepository
import br.edu.uaifood.domain.entities.OrderStatus.*
import br.edu.uaifood.ports.outbound.repository.order.OrderPersisted
import io.mockk.mockk
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import br.edu.uaifood.ports.outbound.repository.product.ProductPersisted
import io.github.glytching.junit.extension.random.Random
import io.github.glytching.junit.extension.random.RandomBeansExtension
import io.mockk.every
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime
import kotlin.test.Test

@ExtendWith(RandomBeansExtension::class)
class OrderServiceImplTest {
    private val orderRepository: OrderRepository = mockk()
    private val orderService = OrderServiceImpl(orderRepository)

    @Test
    fun `should find all orders`(
        @Random firstRandomProduct: ProductPersisted,
        @Random secondRandomProduct: ProductPersisted
    ) {
        //given
        val firstOrder = OrderPersisted(1, listOf(firstRandomProduct), RECEIVED, LocalDateTime.parse("2023-06-20T19:34:50.63"))
        val secondOrder = OrderPersisted(2, listOf(firstRandomProduct, secondRandomProduct), IN_PREPARATION, LocalDateTime.parse("2023-12-23T07:12:10.02"))

        every { orderRepository.findAll() } returns listOf(firstOrder, secondOrder)

        //when
        val orders = orderService.findAllOrders()

        //then
        assertThat(orders[0].status).isEqualTo(RECEIVED)
        assertThat(orders[0].creationDate).isEqualTo("2023-06-20T19:34:50.630")
        assertThat(orders[0].products.size).isEqualTo(1)
        assertThat(orders[0].products[0].name).isEqualTo(firstRandomProduct.name)
        assertThat(orders[1].status).isEqualTo(IN_PREPARATION)
        assertThat(orders[1].creationDate).isEqualTo("2023-12-23T07:12:10.020")
        assertThat(orders[1].products.size).isEqualTo(2)
        assertThat(orders[1].products[0].name).isEqualTo(firstRandomProduct.name)
        assertThat(orders[1].products[1].name).isEqualTo(secondRandomProduct.name)
    }
}