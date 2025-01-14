package br.edu.uaifood.usecases

import br.edu.uaifood.adapters.repositories.OrderRepository
import br.edu.uaifood.adapters.usecases.FindAllOrdersUseCase
import br.edu.uaifood.ports.inbound.api.order.dto.OrderResponse
import org.slf4j.LoggerFactory

class FindAllOrdersUseCaseImpl(var repository: OrderRepository) : FindAllOrdersUseCase {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun execute(): List<OrderResponse> {
        logger.info("Getting order list")
        return runCatching {
            repository.findAll().map { OrderResponse.from(it) }
        }.onFailure { logger.info("Fail to find orders")
        }.getOrThrow()
    }
}