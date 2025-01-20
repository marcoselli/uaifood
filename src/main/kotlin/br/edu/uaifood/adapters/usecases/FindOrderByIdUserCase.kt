package br.edu.uaifood.adapters.usecases

import br.edu.uaifood.ports.inbound.api.order.dto.OrderStatusResponse

interface FindOrderByIdUserCase {
    fun execute(orderId: Long) : OrderStatusResponse
}
