package br.edu.uaifood.usecases

import br.edu.uaifood.adapters.repositories.ProductRepository
import br.edu.uaifood.ports.inbound.api.product.dto.ProductResponse
import br.edu.uaifood.ports.outbound.repository.product.ProductPersisted
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertTrue

class FindProductsByCategoryUseCaseImplTest {

    private val productRepository: ProductRepository = mockk()
    private val findProductsByCategoryUseCaseImpl = FindProductsByCategoryUseCaseImpl(productRepository)

    @Test
    fun `should find a list of products given a category name`() {
        // Given
        val category = "SNACK"
        val returnProducts = List(10) {
            ProductPersisted(
                id = UUID.randomUUID(),
                name = "Chips",
                category = category,
                price = 1.99,
                description = "Crocante e saboroso",
                imageUrl = "http://example.com/chips.png"
            )
        }

        every { productRepository.findByCategory(category)} returns returnProducts

        // When
        val result: List<ProductResponse> = findProductsByCategoryUseCaseImpl.execute(category)

        // Then
        assertTrue(result.all { it.category == category })
        verify(exactly = 1) { productRepository.findByCategory(category) }
    }

}