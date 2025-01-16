package br.edu.uaifood.usecases

import br.edu.uaifood.adapters.repositories.OrderRepository
import br.edu.uaifood.adapters.usecases.CreateOrderUseCase
import br.edu.uaifood.adapters.OrderService
import br.edu.uaifood.adapters.usecases.FindProductsByIdsUseCase
import br.edu.uaifood.domain.entities.Order
import br.edu.uaifood.ports.inbound.api.order.dto.OrderResponse
import br.edu.uaifood.ports.outbound.repository.order.OrderPersisted
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class CreateOrderUseCaseImpl(
    var orderRepository: OrderRepository,
    var findProductsByIdsUseCase: FindProductsByIdsUseCase
): CreateOrderUseCase {
    private val logger = LoggerFactory.getLogger(this::class.java)
    override fun execute(order: Order): OrderResponse {
        logger.info("Creating order")

        val managedProducts = findProductsByIdsUseCase.execute(order.products)
        val orderPersisted = OrderPersisted.from(order).apply { products = managedProducts }

        return runCatching {
            orderRepository.save(orderPersisted)
                .let { OrderResponse.from(it) }
        }.onSuccess { logger.info("Order created successfully")
        }.onFailure { logger.info("Fail to create order: ${it.message}")
        }.getOrThrow()
    }
}
