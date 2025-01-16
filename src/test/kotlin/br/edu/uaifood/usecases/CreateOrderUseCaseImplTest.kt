package br.edu.uaifood.usecases

import br.edu.uaifood.adapters.repositories.OrderRepository
import br.edu.uaifood.domain.entities.*
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test
import kotlin.test.assertFailsWith
import java.time.LocalDateTime

class CreateOrderUseCaseImplTest {

    private val orderRepository: OrderRepository = mockk()
    private val createOrderUseCaseImpl = CreateOrderUseCaseImpl(orderRepository)

    @Test
    fun `should throw an exception when repository fails`() {
        // given
        val products = listOf(
            Product(
                name = "Burger",
                description = "Delicious beef burger",
                price = 15.0,
                category = ProductCategory.SNACK,
                imageUrl = "http://example.com/burger.jpg"
            )
        )
        val order = Order(
            products = products,
            status = OrderStatus.RECEIVED,
            creationDate = LocalDateTime.now(),
            customerCpf = "123.456.789-00"
        )

        every { orderRepository.save(any()) } throws RuntimeException("Database error")

        // when & then
        val exception = assertFailsWith<RuntimeException> {
            createOrderUseCaseImpl.execute(order)
        }
        assertThat(exception.message).isEqualTo("Database error")
    }
}
