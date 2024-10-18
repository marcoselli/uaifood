package br.edu.uaifood.domain.services

import br.edu.uaifood.adapters.ProductRepository
import br.edu.uaifood.domain.entities.Product
import br.edu.uaifood.ports.inbound.api.ProductResponse
import br.edu.uaifood.ports.outbound.repository.ProductPersisted
import io.github.glytching.junit.extension.random.Random
import io.github.glytching.junit.extension.random.RandomBeansExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.kotlin.stub


@MockitoSettings
@ExtendWith(RandomBeansExtension::class)
class ProductServiceImplTest {
    @Mock
    private lateinit var productRepository: ProductRepository
    @InjectMocks
    private lateinit var productService: ProductServiceImpl

    @Test
    fun `should insert a product into menu successfully`(
        @Random randomProduct: Product) {
        //Arrange
        productRepository.stub {
            on { findByName(anyString()) }.thenReturn(emptyList())
            on { save(any(ProductPersisted::class.java)) }.thenReturn(ProductPersisted.from(randomProduct))
        }
        val expected = ProductResponse(
            name = randomProduct.name,
            description = randomProduct.description,
            price = randomProduct.price,
            category = randomProduct.category.name
        )
        // Act
        val result = productService.insertIntoMenu(randomProduct)
        // Assert
        assertEquals(expected.name, result.name)
        assertEquals(expected.description, result.description)
        assertEquals(expected.price, result.price)
        assertEquals(expected.category, result.category)

    }
}