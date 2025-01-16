package br.edu.uaifood.usecases

import br.edu.uaifood.adapters.repositories.OrderRepository
import br.edu.uaifood.adapters.usecases.CreateOrderUseCase
import br.edu.uaifood.domain.entities.Order
import br.edu.uaifood.ports.inbound.api.order.dto.OrderResponse
import br.edu.uaifood.ports.outbound.repository.order.OrderPersisted
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class CreateOrderUseCaseImpl(var repository: OrderRepository): CreateOrderUseCase {
    private val logger = LoggerFactory.getLogger(this::class.java)
    override fun execute(order: Order): OrderResponse {
        logger.info("Creating order")
        return runCatching {
            repository.save(OrderPersisted.from(order))
                .let { OrderResponse.from(it) }
        }.onSuccess { logger.info("Order created successfully")
        }.onFailure { logger.info("Fail to create order: ${it.message}")
        }.getOrThrow()
    }
}