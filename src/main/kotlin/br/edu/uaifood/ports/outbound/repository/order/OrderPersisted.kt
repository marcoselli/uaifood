package br.edu.uaifood.ports.outbound.repository.order

import br.edu.uaifood.domain.entities.*
import br.edu.uaifood.ports.outbound.repository.product.ProductPersisted
import jakarta.persistence.*

@Entity(name = "food_order")
data class OrderPersisted(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,
    @ManyToMany
    @JoinTable(
        name = "food_order_product",
        joinColumns = [JoinColumn(name = "food_order_id")],
        inverseJoinColumns = [JoinColumn(name = "product_id")]
    )
    var products: List<ProductPersisted> = emptyList(),
    @Enumerated(EnumType.STRING)
    var status: OrderStatus
) {
    companion object {
        fun from(order: Order): OrderPersisted {
            return OrderPersisted(
                id = null,
                status = order.status,
                products = order.products.map { ProductPersisted.from(it) }
            )
        }
    }
}