package br.edu.uaifood.ports.outbound.repository.order

import br.edu.uaifood.domain.entities.*
import jakarta.persistence.*

@Entity(name = "order")
data class OrderPersisted(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,
    @OneToMany(mappedBy="product")
//    var products: List<Product>,
    @Enumerated(EnumType.STRING)
    var status: OrderStatus
) {
    companion object {
        fun from(order: Order): OrderPersisted {
            return OrderPersisted(
                id = null,
//                products = order.products,
                status = order.status
            )
        }
    }
}