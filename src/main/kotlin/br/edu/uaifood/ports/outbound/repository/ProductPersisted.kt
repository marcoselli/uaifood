package br.edu.uaifood.ports.outbound.repository

import br.edu.uaifood.domain.entities.Product
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.*

@Entity(name = "product")
data class ProductPersisted(
    @Id
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val imageUrl: String
) {
    companion object {
        fun from(product: Product): ProductPersisted =
            ProductPersisted(
                name = product.name,
                description = product.description,
                price = product.price,
                category = product.category.name,
                imageUrl = product.imageUrl
            )
    }
}