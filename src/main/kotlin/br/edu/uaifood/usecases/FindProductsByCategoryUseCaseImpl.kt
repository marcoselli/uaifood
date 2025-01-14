package br.edu.uaifood.usecases

import br.edu.uaifood.adapters.repositories.ProductRepository
import br.edu.uaifood.adapters.usecases.FindProductsByCategoryUseCase
import br.edu.uaifood.ports.inbound.api.product.dto.ProductResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class FindProductsByCategoryUseCaseImpl(
    private val productRepository: ProductRepository
): FindProductsByCategoryUseCase {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun execute(category: String): List<ProductResponse> {
        logger.info("Getting products by category $category")
        val products = productRepository.findByCategory(category)
        return products.map { product ->
            ProductResponse.from(product)
        }
    }
}