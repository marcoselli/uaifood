package br.edu.uaifood.ports.outbound.repository.product

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
        fun from(newProduct: Product): ProductPersisted =
            ProductPersisted(
                name = newProduct.name,
                description = newProduct.description,
                price = newProduct.price,
                category = newProduct.category.name,
                imageUrl = newProduct.imageUrl
            )

        fun from(existingId: UUID, updatedProduct: Product): ProductPersisted =
            ProductPersisted(
                id = existingId,
                name = updatedProduct.name,
                description = updatedProduct.description,
                price = updatedProduct.price,
                category = updatedProduct.category.name,
                imageUrl = updatedProduct.imageUrl
            )
    }
}