package br.edu.uaifood.domain.entities

import br.edu.uaifood.exception.OrderAlreadyFinishedException
import br.edu.uaifood.ports.inbound.api.order.dto.OrderRequest
import br.edu.uaifood.ports.outbound.repository.order.OrderPersisted
import java.time.LocalDateTime

class Order(
    var products: List<Product> = emptyList(),
    var status: OrderStatus,
    var creationDate: LocalDateTime,
    var customerCpf: String?
) {

    fun nextStatus() {
        when (status) {
            OrderStatus.READY -> this.status = OrderStatus.IN_PREPARATION
            OrderStatus.IN_PREPARATION -> this.status = OrderStatus.RECEIVED
            OrderStatus.RECEIVED -> this.status = OrderStatus.FINISHED
            OrderStatus.FINISHED -> throw OrderAlreadyFinishedException()
        }
    }

    companion object {
        fun from(request: OrderRequest, cpf: String?) =
            Order(
                products = request.products.map { Product.from(it) },
                creationDate = LocalDateTime.now(),
                status = OrderStatus.RECEIVED,
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
    READY(0),
    IN_PREPARATION(1),
    RECEIVED(2),
    FINISHED(3)
}