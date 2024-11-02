package br.edu.uaifood.domain.entities

import br.edu.uaifood.ports.inbound.api.order.dto.OrderRequest
import java.time.LocalDateTime

class Order(
    var products: List<Product> = emptyList(),
    var status: OrderStatus,
    var creationDate: LocalDateTime,
    var customerCpf: String?
) {
    companion object {
        fun from(request: OrderRequest, cpf: String?) =
            Order(
                products = request.products.map { Product.from(it) },
                creationDate = LocalDateTime.now(),
                status = OrderStatus.RECEIVED,
                customerCpf = cpf
            )
    }
}

enum class OrderStatus { RECEIVED, IN_PREPARATION, READY, FINISHED }