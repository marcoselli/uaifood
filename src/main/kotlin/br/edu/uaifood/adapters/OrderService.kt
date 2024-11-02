package br.edu.uaifood.adapters

import br.edu.uaifood.ports.inbound.api.order.dto.OrderResponse

interface OrderService {
    fun findAllOrders(): List<OrderResponse>
}