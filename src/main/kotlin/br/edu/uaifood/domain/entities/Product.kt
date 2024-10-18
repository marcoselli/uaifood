package br.edu.uaifood.domain.entities

import br.edu.uaifood.ports.inbound.api.ProductRequest
import br.edu.uaifood.ports.outbound.repository.ProductPersisted

data class Product(
    val name: String,
    val description: String,
    val price: Double,
    val category: ProductCategory
) {
    companion object {
        fun from(productRequest: ProductRequest) =
            Product(
                name = productRequest.name,
                description = productRequest.description,
                price = validatePrice(productRequest.price),
                category = validateCategory(productRequest.category)
            )

        fun from(productPersisted: ProductPersisted) =
            Product(
                name = productPersisted.name,
                description = productPersisted.description,
                price = validatePrice(productPersisted.price),
                category = validateCategory(productPersisted.category)
            )

        private fun validatePrice(price: Double) =
            if (price > 0) price else throw Exception("Product price should be greater than zero")

        private fun validateCategory(productCategory: String) =
            when (productCategory) {
                "SNACK" -> ProductCategory.SNACK
                "SIDE_DISH" -> ProductCategory.SIDE_DISH
                "DRINK" -> ProductCategory.DRINK
                "DESSERT" -> ProductCategory.DESSERT
                else -> throw Exception("Unknown product category")
            }
    }

    fun ensureUniqueness(products: List<Product>) = products.forEach { this.ensureUniqueness(it) }

    private fun ensureUniqueness(product: Product) {
        if ((this.name == product.name) and (this.category == product.category))
            throw Exception("Product ${this.name} already exists")
        if ((this.name == product.name) and (this.category != product.category))
            throw Exception("Product ${this.name} already exists in another category")
    }


}

enum class ProductCategory {
    SNACK,  SIDE_DISH, DRINK, DESSERT,
}