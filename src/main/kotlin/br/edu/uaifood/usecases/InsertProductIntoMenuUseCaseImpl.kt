package br.edu.uaifood.usecases

import br.edu.uaifood.adapters.repositories.ProductRepository
import br.edu.uaifood.adapters.usecases.InsertProductIntoMenuUseCase
import br.edu.uaifood.domain.entities.Product
import br.edu.uaifood.ports.inbound.api.product.dto.ProductResponse
import br.edu.uaifood.ports.outbound.repository.product.ProductPersisted
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class InsertProductIntoMenuUseCaseImpl(
    private val productRepository: ProductRepository
): InsertProductIntoMenuUseCase {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun execute(product: Product): ProductResponse {
        return runCatching {
            productRepository.findByName(product.name)
                ?.let { Product.from(it) }
                ?.also { alreadySavedProduct -> alreadySavedProduct.ensureUniqueness(product) }

            productRepository.save(ProductPersisted.from(product))
                .let { ProductResponse.from(it) }
        }.onSuccess { logger.info("Product ${product.name} inserted into menu successfully")
        }.onFailure {
            logger.info("Fail to insert product ${product.name}: ${it.message}")
            throw it
        }.getOrThrow()
    }
}