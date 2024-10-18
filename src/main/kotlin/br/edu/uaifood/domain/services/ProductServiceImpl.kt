package br.edu.uaifood.domain.services

import br.edu.uaifood.adapters.ProductService
import br.edu.uaifood.domain.entities.Product
import br.edu.uaifood.ports.inbound.api.ProductResponse
import br.edu.uaifood.ports.outbound.repository.ProductPersisted
import br.edu.uaifood.adapters.ProductRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ProductServiceImpl(
    private val productRepository: ProductRepository
): ProductService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun insertIntoMenu(product: Product): ProductResponse {
        logger.info("Inserting product ${product.name} into menu")
        return productRepository.findByName(product.name)
            .let { it.map { productPersisted -> Product.from(productPersisted) } }
            .let { productRepository.save(ProductPersisted.from(product)) }
            .let { ProductResponse.from(it) }
            .also { logger.info("Product ${product.name} inserted into menu successfully") }
    }
}