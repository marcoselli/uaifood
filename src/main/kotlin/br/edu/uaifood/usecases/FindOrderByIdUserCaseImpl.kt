package br.edu.uaifood.usecases

import br.edu.uaifood.adapters.repositories.OrderRepository
import br.edu.uaifood.adapters.usecases.FindOrderByIdUserCase
import br.edu.uaifood.domain.entities.Order
import br.edu.uaifood.exception.OrderNotFoundException
import br.edu.uaifood.ports.inbound.api.order.dto.OrderStatusResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class FindOrderByIdUseCaseImpl(
    private val repository: OrderRepository
) : FindOrderByIdUserCase {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun execute(orderId: Long): OrderStatusResponse {
        logger.info("Finding order with ID: $orderId")
        return try {
            val orderPersisted = repository.findById(orderId)
                .orElseThrow { OrderNotFoundException(orderId) }
            val order = Order.from(orderPersisted)
            OrderStatusResponse(order.id, order.status)
        } catch (ex: Exception) {
            logger.error("Failed to find order with ID: $orderId - ${ex.message}")
            throw ex
        }
    }
}
