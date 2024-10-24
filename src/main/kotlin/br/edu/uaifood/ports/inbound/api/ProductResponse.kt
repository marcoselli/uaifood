package br.edu.uaifood.ports.inbound.api

import br.edu.uaifood.ports.outbound.repository.ProductPersisted

data class ProductResponse(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val imageUrl: String
) {
    companion object {
        fun from(productPersisted: ProductPersisted): ProductResponse =
            ProductResponse(
                id = productPersisted.id.toString(),
                name = productPersisted.name,
                description = productPersisted.description,
                price = productPersisted.price,
                category = productPersisted.category,
                imageUrl = productPersisted.imageUrl
            )
    }
}