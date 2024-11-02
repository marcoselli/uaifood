package br.edu.uaifood.ports.inbound.api.order.dto

import br.edu.uaifood.domain.entities.OrderStatus
import br.edu.uaifood.domain.entities.Product
import br.edu.uaifood.ports.outbound.repository.order.OrderPersisted

data class OrderResponse(
//    var products: List<Product>,
    var status: OrderStatus
) {
    companion object {
        fun from(orderPersisted: OrderPersisted): OrderResponse =
            OrderResponse(
//                products = orderPersisted.products,
                status = orderPersisted.status
            )
    }
}