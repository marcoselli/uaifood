package br.edu.uaifood.usecases

import br.edu.uaifood.adapters.repositories.ProductRepository
import br.edu.uaifood.ports.outbound.repository.product.ProductPersisted
import io.github.glytching.junit.extension.random.Random
import io.github.glytching.junit.extension.random.RandomBeansExtension
import io.mockk.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(RandomBeansExtension::class)
class RemoveProductFromMenuUseCaseImplTest {

    private val productRepository: ProductRepository = mockk()
    private val removeProductFromMenuUseCaseImpl = RemoveProductFromMenuUseCaseImpl(productRepository)

    @Test
    fun `should remove a product from menu successfully`(@Random randomProduct: ProductPersisted) {
        // Given
        every { productRepository.findByName(any()) } returns randomProduct
        every { productRepository.deleteById(any()) } just Runs
        // When
        removeProductFromMenuUseCaseImpl.execute("ANY_PRODUCT_NAME_HERE")
        // Then
        verify { productRepository.deleteById(any()) }
    }

    @Test
    fun `should do nothing when product name was not found`() {
        // Given
        every { productRepository.findByName(any()) } returns null
        // When
        removeProductFromMenuUseCaseImpl.execute("ANY_PRODUCT_NAME_HERE")
        // Then
        verify(exactly = 0) { productRepository.deleteById(any()) }
    }
}