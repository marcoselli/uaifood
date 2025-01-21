package br.edu.uaifood.usecases

import br.edu.uaifood.adapters.repositories.OrderRepository
import br.edu.uaifood.adapters.usecases.UpdateOrderStatusUseCase
import br.edu.uaifood.domain.entities.Order
import br.edu.uaifood.exception.OrderNotFoundException
import br.edu.uaifood.ports.outbound.repository.order.OrderPersisted
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import kotlin.jvm.optionals.getOrNull

@Component
class UpdateOrderStatusUseCaseImpl(
    private val repository: OrderRepository
): UpdateOrderStatusUseCase {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun execute(orderId: Long) {
        runCatching {
            val orderPersisted = repository.findById(orderId).getOrNull() ?: throw OrderNotFoundException(orderId)
            val order = Order.from(orderPersisted)
            order.nextStatus()
            repository.save(OrderPersisted.from(order, orderPersisted.id))
        }.onSuccess {
            logger.info("Order id $orderId status updated successfully")
        }.onFailure {
            logger.error("Fail to update order $orderId status - ${it.message}")
            throw it
        }
    }
}