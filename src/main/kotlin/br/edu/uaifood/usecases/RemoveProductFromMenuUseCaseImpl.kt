package br.edu.uaifood.usecases

import br.edu.uaifood.adapters.repositories.ProductRepository
import br.edu.uaifood.adapters.usecases.RemoveProductFromMenuUseCase
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class RemoveProductFromMenuUseCaseImpl(
    private val productRepository: ProductRepository
): RemoveProductFromMenuUseCase {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun execute(productName: String) {
        productRepository.findByName(productName)
            ?.let { productRepository.deleteById(it.id) }
            ?.also { logger.info("Product $productName removed from menu") }
    }
}