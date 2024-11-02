package br.edu.uaifood.ports.inbound.api.order

import br.edu.uaifood.adapters.OrderService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity.*
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/orders")
class OrderController(var service: OrderService) {

    @Operation(summary = "Get a list of orders", description = "Returns 200 if successful")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Orders"),
        ]
    )
    @GetMapping
    fun findOrders() =
        service.findAllOrders()
            .let { status(OK).body(it) }
}