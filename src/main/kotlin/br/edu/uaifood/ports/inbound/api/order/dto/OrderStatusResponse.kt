package br.edu.uaifood.ports.inbound.api.order.dto

import br.edu.uaifood.domain.entities.OrderStatus

data class OrderStatusResponse(
    var orderId: Long? = null,
    var status: OrderStatus
)
