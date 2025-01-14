package br.edu.uaifood.usecases

import br.edu.uaifood.adapters.repositories.ProductRepository
import br.edu.uaifood.adapters.usecases.UpdateMenuProductUseCase
import br.edu.uaifood.domain.entities.Product
import br.edu.uaifood.exception.ProductNotFoundException
import br.edu.uaifood.ports.inbound.api.product.dto.ProductResponse
import br.edu.uaifood.ports.outbound.repository.product.ProductPersisted
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class UpdateMenuProductUseCaseImpl(
    private val productRepository: ProductRepository
): UpdateMenuProductUseCase {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun execute(productName: String, updatedProduct: Product): ProductResponse {
        return runCatching {
            val oldProduct = productRepository.findByName(productName)
                ?: throw ProductNotFoundException("Product $productName not found")
            productRepository.save(ProductPersisted.from(oldProduct.id, updatedProduct))
                .let { ProductResponse.from(it) }
        }.onSuccess { logger.info("Product $productName updated from menu successfully")
        }.onFailure { logger.info("Fail to update product $productName: ${it.message}")
        }.getOrThrow()
    }
}