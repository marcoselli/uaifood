package br.edu.uaifood.adapters

import br.edu.uaifood.domain.entities.Order
import br.edu.uaifood.domain.entities.OrderStatus.READY
import br.edu.uaifood.domain.entities.OrderStatus.RECEIVED
import br.edu.uaifood.ports.outbound.repository.order.OrderPersisted
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import kotlin.test.Test

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    lateinit var entityManager: TestEntityManager

    @Autowired
    lateinit var orderRepository: OrderRepository

    @Test
    fun `should find a customer by cpf successfully`() {
        //given
        entityManager.persist(OrderPersisted.from(Order(RECEIVED)))
        entityManager.persist(OrderPersisted.from(Order(READY)))

        //when
        val orders = orderRepository.findAll()

        //then
        assertThat(orders[0].status).isEqualTo(RECEIVED)
        assertThat(orders[1].status).isEqualTo(READY)
    }

    @Test
    fun `should return empty list if there is no orders`() {
        //when
        val orders = orderRepository.findAll()

        //then
        assertThat(orders.isEmpty())
    }
}