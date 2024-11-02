package br.edu.uaifood.ports.inbound.api.order

import br.edu.uaifood.adapters.CheckoutService
import br.edu.uaifood.adapters.OrderService
import br.edu.uaifood.domain.entities.Order
import br.edu.uaifood.exception.ProductValidationException
import br.edu.uaifood.ports.inbound.api.order.dto.OrderRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.*
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/orders")
class OrderController(
    var orderService: OrderService,
    var checkoutService: CheckoutService
) {

    @Operation(summary = "Get a list of orders", description = "Returns 200 if successful")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Orders"),
        ]
    )
    @GetMapping
    fun findOrders() =
        orderService.findAllOrders()
            .let { status(OK).body(it) }

    @Operation(summary = "Create a order", description = "Returns 201 if successful")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Order Created"),
            ApiResponse(responseCode = "400", description = "Error creating Order"),
        ]
    )
    @PostMapping
    fun createOrder(@RequestBody orderRequest: OrderRequest): ResponseEntity<Any> {
        val paymentConfirmed = checkoutService.fakeCheckout()
        if (paymentConfirmed) {
            return orderService.createOrder(Order.from(orderRequest))
                .let { status(HttpStatus.CREATED).body(it) }
        } else {
            throw ProductValidationException("There was a problem with payment and the order was not received")
        }
    }
}