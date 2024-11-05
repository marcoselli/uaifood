package br.edu.uaifood.domain.entities

import br.edu.uaifood.exception.ProductValidationException
import br.edu.uaifood.ports.inbound.api.product.dto.UpsertProductRequest
import br.edu.uaifood.ports.outbound.repository.product.ProductPersisted

data class Product(
    val name: String,
    val description: String,
    val price: Double,
    val category: ProductCategory,
    val imageUrl: String
) {
    companion object {
        fun from(upsertProductRequest: UpsertProductRequest) =
            Product(
                name = upsertProductRequest.name,
                description = upsertProductRequest.description,
                price = validatePrice(upsertProductRequest.price),
                category = validateCategory(upsertProductRequest.category),
                imageUrl = upsertProductRequest.imageUrl
            )

        fun from(productPersisted: ProductPersisted) =
            Product(
                name = productPersisted.name,
                description = productPersisted.description,
                price = validatePrice(productPersisted.price),
                category = validateCategory(productPersisted.category),
                imageUrl = productPersisted.imageUrl

            )

        private fun validatePrice(price: Double) =
            if (price > 0) price else throw ProductValidationException("Product price should be greater than zero")

        private fun validateCategory(productCategory: String) =
            when (productCategory) {
                "SNACK" -> ProductCategory.SNACK
                "SIDE_DISH" -> ProductCategory.SIDE_DISH
                "DRINK" -> ProductCategory.DRINK
                "DESSERT" -> ProductCategory.DESSERT
                else -> throw ProductValidationException("Unknown product category")
            }
    }

    fun ensureUniqueness(product: Product) {
        if ((this.name == product.name) and (this.category == product.category))
            throw ProductValidationException("Product ${this.name} already exists")
        if ((this.name == product.name) and (this.category != product.category))
            throw ProductValidationException("Product ${this.name} already exists in another category")
    }

}

enum class ProductCategory {
    SNACK,  SIDE_DISH, DRINK, DESSERT,
}