package br.edu.uaifood.ports.inbound.api.order.dto

import br.edu.uaifood.domain.entities.Order
import br.edu.uaifood.domain.entities.OrderStatus
import br.edu.uaifood.domain.entities.Product
import br.edu.uaifood.ports.inbound.api.product.dto.ProductResponse
import br.edu.uaifood.ports.outbound.repository.order.OrderPersisted

data class OrderResponse(
    var orderId: Long?,
    var products: List<ProductResponse> = emptyList(),
    var status: OrderStatus,
    var creationDate: String
) {
    companion object {
        fun from(orderPersisted: OrderPersisted): OrderResponse =
            OrderResponse(
                orderId = orderPersisted.id,
                products = orderPersisted.products.map { ProductResponse.from(it) },
                status = orderPersisted.status,
                creationDate = orderPersisted.creationDate.toString()
            )

        fun from(order: Order): OrderResponse =
            OrderResponse(
                products = order.products.map { ProductResponse.from(it) },
                status = order.status,
                creationDate = order.creationDate.toString()
            )
    }
}
