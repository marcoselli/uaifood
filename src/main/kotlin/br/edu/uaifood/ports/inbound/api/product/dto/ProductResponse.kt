package br.edu.uaifood.ports.inbound.api.product.dto

import br.edu.uaifood.domain.entities.Product
import br.edu.uaifood.ports.outbound.repository.product.ProductPersisted

data class ProductResponse(
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val imageUrl: String
) {
    companion object {
        fun from(productPersisted: ProductPersisted): ProductResponse =
            ProductResponse(
                name = productPersisted.name,
                description = productPersisted.description,
                price = productPersisted.price,
                category = productPersisted.category,
                imageUrl = productPersisted.imageUrl
            )

        fun from(product: Product): ProductResponse =
            ProductResponse(
                name = product.name,
                description = product.description,
                price = product.price,
                category = product.category.name,
                imageUrl = product.imageUrl
            )

    }
}