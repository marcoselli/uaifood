package br.edu.uaifood.ports.inbound.api.order.dto

import br.edu.uaifood.domain.entities.OrderStatus
import br.edu.uaifood.ports.inbound.api.product.dto.ProductResponse
import br.edu.uaifood.ports.outbound.repository.order.OrderPersisted
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class OrderResponse(
    var products: List<ProductResponse> = emptyList(),
    var status: OrderStatus,
    var creationDate: String
) {
    companion object {
        fun from(orderPersisted: OrderPersisted): OrderResponse =
            OrderResponse(
                products = orderPersisted.products.map { ProductResponse.from(it) },
                status = orderPersisted.status,
                creationDate = orderPersisted.creationDate.toString()
            )
    }
}