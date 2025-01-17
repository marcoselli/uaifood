package br.edu.uaifood.adapters.usecases

import br.edu.uaifood.domain.entities.Product
import br.edu.uaifood.ports.outbound.repository.product.ProductPersisted

interface FindProductsByIdsUseCase {
    fun execute(products: List<Product>) : List<ProductPersisted>
}
