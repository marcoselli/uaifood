package br.edu.uaifood.domain.services

import br.edu.uaifood.adapters.repositories.OrderRepository
import br.edu.uaifood.adapters.OrderService
import br.edu.uaifood.adapters.repositories.ProductRepository
import br.edu.uaifood.domain.entities.Order
import br.edu.uaifood.ports.inbound.api.order.dto.OrderResponse
import br.edu.uaifood.ports.outbound.repository.order.OrderPersisted
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class OrderServiceImpl(
    var orderRepository: OrderRepository,
    var productRepository: ProductRepository //@todo all logic using it should be in some kind of "ProductServer"
) : OrderService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun findAllOrders(): List<OrderResponse> {        logger.info("Getting order list")
        return runCatching {
            orderRepository.findAll().map { OrderResponse.from(it) }
        }.onFailure { logger.info("Fail to find orders")
        }.getOrThrow()
    }

    override fun createOrder(order: Order): OrderResponse {
        logger.info("Creating order")

        val managedProducts = order.products.map { product ->
            productRepository.findById(product.id ?: throw IllegalArgumentException("Product ID is missing"))
                .orElseThrow { IllegalArgumentException("Product not found: ${product.id}") }
        }
        val orderPersisted = OrderPersisted.from(order).apply { products = managedProducts }

        return runCatching {
//            orderRepository.save(OrderPersisted.from(order))
            orderRepository.save(orderPersisted)
                .let { OrderResponse.from(it) }
        }.onSuccess { logger.info("Order created successfully")
        }.onFailure { logger.info("Fail to create order: ${it.message}")
        }.getOrThrow()
    }
}
