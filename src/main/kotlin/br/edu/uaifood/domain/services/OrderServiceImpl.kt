package br.edu.uaifood.domain.services

import br.edu.uaifood.adapters.OrderRepository
import br.edu.uaifood.adapters.OrderService
import br.edu.uaifood.ports.inbound.api.order.dto.OrderResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class OrderServiceImpl(var repository: OrderRepository) : OrderService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun findAllOrders(): List<OrderResponse> {
        logger.info("Getting order list")
        return runCatching {
            repository.findAll().map { OrderResponse.from(it) }
        }.onFailure { logger.info("Fail to find orders")
        }.getOrThrow()
    }
}