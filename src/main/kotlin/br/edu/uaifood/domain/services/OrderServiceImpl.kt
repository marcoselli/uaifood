package br.edu.uaifood.domain.services

import br.edu.uaifood.adapters.repositories.OrderRepository
import br.edu.uaifood.adapters.OrderService
import br.edu.uaifood.domain.entities.Order
import br.edu.uaifood.ports.inbound.api.order.dto.OrderResponse
import br.edu.uaifood.ports.outbound.repository.order.OrderPersisted
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class OrderServiceImpl(var repository: OrderRepository) : OrderService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun findAllOrders(): List<OrderResponse> {        logger.info("Getting order list")
        return runCatching {
            repository.findAll().map { OrderResponse.from(it) }
        }.onFailure { logger.info("Fail to find orders")
        }.getOrThrow()
    }

    override fun createOrder(order: Order): OrderResponse {
        logger.info("Creating order")
        return runCatching {
            repository.save(OrderPersisted.from(order))
                .let { OrderResponse.from(it) }
        }.onSuccess { logger.info("Order created successfully")
        }.onFailure { logger.info("Fail to create order: ${it.message}")
        }.getOrThrow()
    }
}