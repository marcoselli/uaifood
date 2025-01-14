package br.edu.uaifood.adapters.usecases

import br.edu.uaifood.ports.inbound.api.product.dto.ProductResponse

interface FindProductsByCategoryUseCase {
    fun execute(category: String): List<ProductResponse>
}