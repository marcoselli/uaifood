package br.edu.uaifood.usecases

import br.edu.uaifood.adapters.repositories.ProductRepository
import br.edu.uaifood.adapters.usecases.FindProductsByIdsUseCase
import br.edu.uaifood.domain.entities.Product
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class FindProductsByIdsUseCaseImpl(
    var productRepository: ProductRepository
) : FindProductsByIdsUseCase {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun execute(products: List<Product>) =
        logger.info("Creating order").run {
            products.map { product ->
                productRepository.findById(product.id ?: throw IllegalArgumentException("Product ID is missing"))
                    .orElseThrow { IllegalArgumentException("Product not found: ${product.id}") }
            }
        }
}
