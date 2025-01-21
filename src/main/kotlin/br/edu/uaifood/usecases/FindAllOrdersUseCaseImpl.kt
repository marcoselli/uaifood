package br.edu.uaifood.usecases

import br.edu.uaifood.adapters.repositories.OrderRepository
import br.edu.uaifood.adapters.usecases.FindAllOrdersUseCase
import br.edu.uaifood.domain.entities.Order
import br.edu.uaifood.domain.services.OrderService
import br.edu.uaifood.ports.inbound.api.order.dto.OrderResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class FindAllOrdersUseCaseImpl(
    var repository: OrderRepository,
    var service: OrderService
) : FindAllOrdersUseCase {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun execute(): List<OrderResponse> {
        logger.info("Getting order list")
        return runCatching {
            val allOrders = repository.findAll().map { Order.from(it) }
            val allOrdersSorted = service.retrieveOrdersSortedByPriority(allOrders)
            allOrdersSorted
        }.onFailure {
            logger.info("Fail to find orders - ${it.message}")
            throw it
        }.getOrThrow()
    }
}