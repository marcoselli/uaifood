package br.edu.uaifood.domain.entities

import br.edu.uaifood.exception.OrderAlreadyFinishedException
import br.edu.uaifood.ports.inbound.api.order.dto.OrderRequest
import br.edu.uaifood.ports.outbound.repository.order.OrderPersisted
import java.time.LocalDateTime

data class Order(
    val id: Long? = null,
    val products: List<Product> = emptyList(),
    var status: OrderStatus,
    val creationDate: LocalDateTime,
    val customerCpf: String?
) {

    fun nextStatus() {
        when (status) {
            OrderStatus.WAITING_PAYMENT -> this.status = OrderStatus.RECEIVED
            OrderStatus.RECEIVED -> this.status = OrderStatus.IN_PREPARATION
            OrderStatus.IN_PREPARATION -> this.status = OrderStatus.RECEIVED
            OrderStatus.READY -> this.status = OrderStatus.FINISHED
            OrderStatus.FINISHED -> throw OrderAlreadyFinishedException()
        }
    }

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
                id = orderPersisted.id,
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