package br.edu.uaifood.domain.services

import br.edu.uaifood.domain.entities.Order
import br.edu.uaifood.domain.entities.OrderStatus
import br.edu.uaifood.ports.inbound.api.order.dto.OrderResponse
import org.springframework.stereotype.Service

@Service
class OrderService {
    fun retrieveOrdersSortedByPriority(orders: List<Order>): List<OrderResponse> {
        return orders
            .filterNot { it.status == OrderStatus.FINISHED  }
            .sortedWith(compareBy<Order> {it.status.priority}.thenBy { it.creationDate } )
            .map { OrderResponse.from(it) }
    }
}