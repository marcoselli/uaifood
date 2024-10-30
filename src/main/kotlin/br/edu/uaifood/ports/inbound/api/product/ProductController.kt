package br.edu.uaifood.ports.inbound.api.product

import br.edu.uaifood.domain.entities.Product
import br.edu.uaifood.adapters.ProductService
import br.edu.uaifood.ports.inbound.api.product.dto.UpsertProductRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
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
    @Operation(summary = "Insert new product into menu", description = "Returns 201 if successful")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Product inserted successfully"),
            ApiResponse(responseCode = "400", description = "Error inserting Product"),
        ]
    )
    @PostMapping
    fun insertIntoMenu(@RequestBody upsertProductRequest: UpsertProductRequest) =
        productService.insertIntoMenu(Product.from(upsertProductRequest))
            .let { ResponseEntity.status(HttpStatus.CREATED).body(it) }
}