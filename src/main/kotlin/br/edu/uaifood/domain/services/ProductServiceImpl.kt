package br.edu.uaifood.domain.services

import br.edu.uaifood.adapters.ProductService
import br.edu.uaifood.domain.entities.Product
import br.edu.uaifood.ports.inbound.api.product.dto.ProductResponse
import br.edu.uaifood.ports.outbound.repository.product.ProductPersisted
import br.edu.uaifood.adapters.ProductRepository
import br.edu.uaifood.domain.entities.ProductCategory
import br.edu.uaifood.exception.ProductNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ProductServiceImpl(
    private val productRepository: ProductRepository
): ProductService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun insertIntoMenu(product: Product): ProductResponse {
        return runCatching {
            productRepository.findByName(product.name)
            ?.let { Product.from(it) }
            ?.also { alreadySavedProduct -> alreadySavedProduct.ensureUniqueness(product) }

            productRepository.save(ProductPersisted.from(product))
            .let { ProductResponse.from(it) }
        }.onSuccess { logger.info("Product ${product.name} inserted into menu successfully")
        }.onFailure { logger.info("Fail to insert product ${product.name}: ${it.message}")
        }.getOrThrow()
    }

    override fun updateMenuProduct(productName: String, updatedProduct: Product): ProductResponse {
        return runCatching {
            val oldProduct = productRepository.findByName(productName)
                ?: throw ProductNotFoundException("Product $productName not found")
            productRepository.save(ProductPersisted.from(oldProduct.id, updatedProduct))
                .let { ProductResponse.from(it) }
        }.onSuccess { logger.info("Product $productName updated from menu successfully")
        }.onFailure { logger.info("Fail to update product $productName: ${it.message}")
        }.getOrThrow()
    }

    override fun removeFromMenu(productName: String) {
        productRepository.findByName(productName)
            ?.let { productRepository.deleteById(it.id) }
            ?.also { logger.info("Product $productName removed from menu") }
    }

    override fun findProductsByCategory(category: String): List<ProductResponse> {
        logger.info("Getting products by category $category")
        val products = productRepository.findByCategory(category)
        return products.map { product ->
            ProductResponse.from(product)
        }
    }
}