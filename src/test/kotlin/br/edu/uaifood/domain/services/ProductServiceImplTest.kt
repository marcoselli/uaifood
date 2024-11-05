package br.edu.uaifood.domain.services

import br.edu.uaifood.adapters.ProductRepository
import br.edu.uaifood.domain.entities.Product
import br.edu.uaifood.exception.ProductNotFoundException
import br.edu.uaifood.ports.inbound.api.product.dto.ProductResponse
import br.edu.uaifood.ports.outbound.repository.product.ProductPersisted
import io.github.glytching.junit.extension.random.Random
import io.github.glytching.junit.extension.random.RandomBeansExtension
import io.mockk.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*
import kotlin.test.assertContains
import kotlin.test.assertTrue


@ExtendWith(RandomBeansExtension::class)
class ProductServiceImplTest {
    private val productRepository: ProductRepository = mockk()
    private val productService = ProductServiceImpl(productRepository)

    @Test
    fun `should insert a product into menu successfully`(
        @Random randomProduct: Product
    ) {
        // Given
        every { productRepository.findByName(any()) } returns null
        val productPersisted = ProductPersisted.from(randomProduct)
        every { productRepository.save(any()) } returns productPersisted
        val expected = ProductResponse(
            name = randomProduct.name,
            description = randomProduct.description,
            price = randomProduct.price,
            category = randomProduct.category.name,
            imageUrl = randomProduct.imageUrl,
        )
        // When
        val result = productService.insertIntoMenu(randomProduct)
        // Then
        assertEquals(expected.name, result.name)
        assertEquals(expected.description, result.description)
        assertEquals(expected.price, result.price)
        assertEquals(expected.category, result.category)
    }

    @Test
    fun `should update a product from menu successfully`(
        @Random oldProduct: ProductPersisted,
        @Random randomProduct: Product
    ) {
        // Given
        every { productRepository.findByName(any()) } returns oldProduct
        val productPersisted = ProductPersisted.from(randomProduct)
        every { productRepository.save(any()) } returns productPersisted
        val expected = ProductResponse(
            name = randomProduct.name,
            description = randomProduct.description,
            price = randomProduct.price,
            category = randomProduct.category.name,
            imageUrl = randomProduct.imageUrl,
        )
        // When
        val result = productService.updateMenuProduct(randomProduct.name, randomProduct)
        // Then
        assertEquals(expected.name, result.name)
        assertEquals(expected.description, result.description)
        assertEquals(expected.price, result.price)
        assertEquals(expected.category, result.category)
    }

    @Test
    fun `should throw an error when update and product name dont exist`(
        @Random randomProduct: Product
    ) {
        // Given
        every { productRepository.findByName(any()) } returns null
        // When - Then
        val error = assertThrows<ProductNotFoundException> {
            productService.updateMenuProduct(randomProduct.name, randomProduct)
        }
        assertEquals("Product ${randomProduct.name} not found", error.reason)
        verify(exactly = 1) { productRepository.findByName(any()) }
        verify(exactly = 0) { productRepository.save(any()) }
    }

    @Test
    fun `should remove a product from menu successfully`(@Random randomProduct: ProductPersisted) {
        // Given
        every { productRepository.findByName(any()) } returns randomProduct
        every { productRepository.deleteById(any()) } just Runs
        // When
        productService.removeFromMenu("ANY_PRODUCT_NAME_HERE")
        // Then
        verify { productRepository.deleteById(any()) }
    }

    @Test
    fun `should do nothing when product name was not found`() {
        // Given
        every { productRepository.findByName(any()) } returns null
        // When
        productService.removeFromMenu("ANY_PRODUCT_NAME_HERE")
        // Then
        verify(exactly = 0) { productRepository.deleteById(any()) }
    }

    @Test
    fun `should find a list of products given a category name`() {
        // Given
        val category = "SNACK"
        val returnProducts = List(10){
                    ProductPersisted(
                        id = UUID.randomUUID(),
                        name = "Chips",
                        category = category,
                        price = 1.99,
                        description = "Crocante e saboroso",
                        imageUrl = "http://example.com/chips.png"
                    )}

        every { productRepository.findByCategory(category)} returns returnProducts

        // When
        val result: List<ProductResponse> = productService.findProductsByCategory(category)

        // Then
        assertTrue(result.all { it.category == category })
        verify(exactly = 1) { productRepository.findByCategory(category) }
    }
}