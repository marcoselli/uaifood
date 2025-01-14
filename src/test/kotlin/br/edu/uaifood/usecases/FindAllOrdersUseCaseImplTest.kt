package br.edu.uaifood.usecases

import br.edu.uaifood.adapters.repositories.OrderRepository
import br.edu.uaifood.domain.entities.OrderStatus.IN_PREPARATION
import br.edu.uaifood.domain.entities.OrderStatus.RECEIVED
import br.edu.uaifood.ports.outbound.repository.order.OrderPersisted
import br.edu.uaifood.ports.outbound.repository.product.ProductPersisted
import io.github.glytching.junit.extension.random.Random
import io.github.glytching.junit.extension.random.RandomBeansExtension
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime.parse
import kotlin.test.Test

@ExtendWith(RandomBeansExtension::class)
class FindAllOrdersUseCaseImplTest {
    private val orderRepository: OrderRepository = mockk()
    private val findAllOrdersUseCaseImpl = FindAllOrdersUseCaseImpl(orderRepository)

    @Test
    fun `should find all orders`(
        @Random firstRandomProduct: ProductPersisted,
        @Random secondRandomProduct: ProductPersisted
    ) {
        //given
        val firstOrder = OrderPersisted(1, listOf(firstRandomProduct), RECEIVED, parse("2023-06-20T19:34:50.63"), null)
        val secondOrder = OrderPersisted(2, listOf(firstRandomProduct, secondRandomProduct), IN_PREPARATION, parse("2023-12-23T07:12:10.02"), "910.933.630-37")

        every { orderRepository.findAll() } returns listOf(firstOrder, secondOrder)

        //when
        val orders = findAllOrdersUseCaseImpl.execute()

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