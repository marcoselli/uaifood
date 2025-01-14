package br.edu.uaifood.ports.inbound.api.product.dto

import br.edu.uaifood.ports.outbound.repository.product.ProductPersisted
import java.util.*

data class ProductResponse(
    val id: UUID,
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val imageUrl: String
) {
    companion object {
        fun from(productPersisted: ProductPersisted): ProductResponse =
            ProductResponse(
                id = productPersisted.id,
                name = productPersisted.name,
                description = productPersisted.description,
                price = productPersisted.price,
                category = productPersisted.category,
                imageUrl = productPersisted.imageUrl
            )
    }
}
