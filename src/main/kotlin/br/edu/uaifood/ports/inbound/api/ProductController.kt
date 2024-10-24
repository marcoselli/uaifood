package br.edu.uaifood.ports.inbound.api

import br.edu.uaifood.domain.entities.Product
import br.edu.uaifood.adapters.ProductService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/products")
class ProductController(
    val productService: ProductService
) {
    @PostMapping
    fun insertIntoMenu(@RequestBody productRequest: ProductRequest) =
        productService.insertIntoMenu(Product.from(productRequest))
            .let { ResponseEntity.status(HttpStatus.CREATED).body(it) }
}