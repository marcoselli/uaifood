package br.edu.uaifood.adapters.usecases

import br.edu.uaifood.domain.entities.Order
import br.edu.uaifood.ports.inbound.api.order.dto.OrderResponse

interface CreateOrderUseCase {
    fun execute(order: Order): OrderResponse
}