package br.edu.uaifood.domain.services

import br.edu.uaifood.adapters.ProductRepository
import br.edu.uaifood.domain.entities.Product
import br.edu.uaifood.ports.inbound.api.ProductResponse
import br.edu.uaifood.ports.outbound.repository.ProductPersisted
import io.github.glytching.junit.extension.random.Random
import io.github.glytching.junit.extension.random.RandomBeansExtension
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.UUID


@ExtendWith(RandomBeansExtension::class)
class ProductServiceImplTest {
    private val productRepository: ProductRepository = mockk()
    private val productService = ProductServiceImpl(productRepository)

    @Test
    fun `should insert a product into menu successfully`(
        @Random randomProduct: Product) {
        // Given
        every { productRepository.findByName(any()) } returns emptyList()
        val productPersisted = ProductPersisted.from(randomProduct)
        every { productRepository.save(any()) } returns productPersisted
        val expected = ProductResponse(
            id = productPersisted.id.toString(),
            name = randomProduct.name,
            description = randomProduct.description,
            price = randomProduct.price,
            category = randomProduct.category.name,
            imageUrl = randomProduct.imageUrl,
        )
        // When
        val result = productService.insertIntoMenu(randomProduct)
        // Then
        assertEquals(expected.id, result.id)
        assertEquals(expected.name, result.name)
        assertEquals(expected.description, result.description)
        assertEquals(expected.price, result.price)
        assertEquals(expected.category, result.category)
    }
}