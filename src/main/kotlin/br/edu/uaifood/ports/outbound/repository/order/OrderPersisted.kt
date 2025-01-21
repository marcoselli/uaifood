package br.edu.uaifood.ports.outbound.repository.order

import br.edu.uaifood.domain.entities.*
import br.edu.uaifood.ports.outbound.repository.product.ProductPersisted
import jakarta.persistence.*
import jakarta.persistence.EnumType.STRING
import jakarta.persistence.GenerationType.IDENTITY
import java.time.LocalDateTime


@Entity(name = "food_order")
data class OrderPersisted(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    var id: Long?,
    @ManyToMany
    @JoinTable(
        name = "food_order_product",
        joinColumns = [JoinColumn(name = "food_order_id")],
        inverseJoinColumns = [JoinColumn(name = "product_id")]
    )
    var products: List<ProductPersisted> = emptyList(),
    @Enumerated(STRING)
    var status: OrderStatus,
    var creationDate: LocalDateTime,
    var customerCPF: String?
) {
    companion object {
        fun from(order: Order, orderId: Long? = null): OrderPersisted {
            return OrderPersisted(
                id = orderId,
                status = order.status,
                products = order.products.map { ProductPersisted.from(it) },
                creationDate = order.creationDate,
                customerCPF = order.customerCpf
            )
        }
    }
}