package br.edu.uaifood.adapters

import br.edu.uaifood.adapters.repositories.OrderRepository
import br.edu.uaifood.domain.entities.Order
import br.edu.uaifood.domain.entities.OrderStatus.READY
import br.edu.uaifood.domain.entities.OrderStatus.RECEIVED
import br.edu.uaifood.domain.entities.Product
import br.edu.uaifood.ports.outbound.repository.order.OrderPersisted
import io.github.glytching.junit.extension.random.Random
import io.github.glytching.junit.extension.random.RandomBeansExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime.parse
import kotlin.test.Test

@ExtendWith(RandomBeansExtension::class)
@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    lateinit var entityManager: TestEntityManager

    @Autowired
    lateinit var orderRepository: OrderRepository

    @Test
    fun `should find all orders`(
        @Random firstRandomProduct: Product,
        @Random secondRandomProduct: Product
    ) {
        //given
        entityManager.persist(OrderPersisted.from(Order(1L, listOf(firstRandomProduct), RECEIVED, parse("2023-06-20T19:34:50.63"), null)))
        entityManager.persist(OrderPersisted.from(Order(2L, listOf(firstRandomProduct, secondRandomProduct), READY, parse("2023-12-26T07:12:10.02"), "910.933.630-37")))

        //when
        val orders = orderRepository.findAll()

        //then
        assertThat(orders[0].status).isEqualTo(RECEIVED)
        assertThat(orders[0].creationDate).isEqualTo("2023-06-20T19:34:50.63")
        assertThat(orders[0].customerCPF).isEqualTo(null)
        assertThat(orders[0].products.size).isEqualTo(1)
        assertThat(orders[0].products[0].name).isEqualTo(firstRandomProduct.name)
        assertThat(orders[1].status).isEqualTo(READY)
        assertThat(orders[1].creationDate).isEqualTo("2023-12-26T07:12:10.02")
        assertThat(orders[1].customerCPF).isEqualTo("910.933.630-37")
        assertThat(orders[1].products.size).isEqualTo(2)
        assertThat(orders[1].products[0].name).isEqualTo(firstRandomProduct.name)
        assertThat(orders[1].products[1].name).isEqualTo(secondRandomProduct.name)
    }

    @Test
    fun `should return empty list if there is no orders`() {
        //when
        val orders = orderRepository.findAll()

        //then
        assertThat(orders.isEmpty())
    }
}