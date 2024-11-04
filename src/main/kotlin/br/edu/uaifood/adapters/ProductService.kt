package br.edu.uaifood.adapters

import br.edu.uaifood.domain.entities.Product
import br.edu.uaifood.ports.inbound.api.product.dto.ProductResponse

interface ProductService {
    fun insertIntoMenu(product: Product): ProductResponse
    fun updateMenuProduct(productName: String, updatedProduct: Product): ProductResponse
    fun removeFromMenu(productName: String)
    fun findProductsByCategory(category: String): List<ProductResponse>
}