package br.edu.uaifood.adapters.usecases

import br.edu.uaifood.ports.inbound.api.order.dto.OrderResponse

interface FindAllOrdersUseCase {
    fun execute(): List<OrderResponse>
}