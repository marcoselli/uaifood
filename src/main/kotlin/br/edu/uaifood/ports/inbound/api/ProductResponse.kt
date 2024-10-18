package br.edu.uaifood.ports.inbound.api

import br.edu.uaifood.ports.outbound.repository.ProductPersisted
import java.util.*

data class ProductResponse(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val description: String,
    val price: Double,
    val category: String
) {
    companion object {
        fun from(productPersisted: ProductPersisted): ProductResponse =
            ProductResponse(
                name = productPersisted.name,
                description = productPersisted.description,
                price = productPersisted.price,
                category = productPersisted.category
            )
    }
}