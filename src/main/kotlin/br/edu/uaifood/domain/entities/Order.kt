package br.edu.uaifood.domain.entities

import br.edu.uaifood.ports.inbound.api.order.dto.OrderRequest
import br.edu.uaifood.ports.outbound.repository.order.OrderPersisted
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
                status = OrderStatus.WAITING_PAYMENT,
                customerCpf = cpf
            )

        fun from(orderPersisted: OrderPersisted): Order =
            Order(
                products = orderPersisted.products.map { Product.from(it) },
                creationDate = orderPersisted.creationDate,
                status = orderPersisted.status,
                customerCpf = orderPersisted.customerCPF
            )
    }
}

enum class OrderStatus(val priority: Int) {
    FINISHED(0),
    READY(1),
    IN_PREPARATION(2),
    RECEIVED(3),
    WAITING_PAYMENT(4)
}