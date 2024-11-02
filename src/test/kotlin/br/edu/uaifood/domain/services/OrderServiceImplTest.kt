package br.edu.uaifood.domain.services

import br.edu.uaifood.adapters.OrderRepository
import br.edu.uaifood.domain.entities.OrderStatus
import br.edu.uaifood.ports.outbound.repository.order.OrderPersisted
import io.mockk.mockk
import java.util.ArrayList
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import br.edu.uaifood.domain.entities.CustomerStatus.ACTIVE
import br.edu.uaifood.domain.entities.OrderStatus.READY
import br.edu.uaifood.domain.entities.OrderStatus.RECEIVED
import io.mockk.every
import kotlin.test.Test

class OrderServiceImplTest {
    private val orderRepository: OrderRepository = mockk()
    private val orderService = OrderServiceImpl(orderRepository)

    @Test
    fun `should find the list of orders`() {
        //given
        val orders = ArrayList<OrderPersisted>()
        orders.add(OrderPersisted(1, RECEIVED))
        orders.add(OrderPersisted(2, READY))

        every { orderRepository.findAll() } returns orders

        //when
        val result = orderService.findAllOrders()

        //then
        assertThat(result[0].status).isEqualTo(RECEIVED)
        assertThat(result[1].status).isEqualTo(READY)
    }


}