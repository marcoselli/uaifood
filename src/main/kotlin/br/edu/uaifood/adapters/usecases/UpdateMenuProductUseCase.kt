package br.edu.uaifood.adapters.usecases

import br.edu.uaifood.domain.entities.Product
import br.edu.uaifood.ports.inbound.api.product.dto.ProductResponse

interface UpdateMenuProductUseCase {
    fun execute(productName: String, updatedProduct: Product): ProductResponse
}