package br.edu.uaifood.ports.inbound.api.customer

import br.edu.uaifood.adapters.CustomerService
import br.edu.uaifood.domain.entities.Customer
import br.edu.uaifood.ports.inbound.api.customer.dto.CustomerRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.*
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/customers")
class CustomerController(var service: CustomerService) {

    @Operation(summary = "Creates a new customer", description = "Returns 201 if successful")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Customer created"),
            ApiResponse(responseCode = "400", description = "Error creating Customer"),
        ]
    )
    @PostMapping
    fun createCustomer(@RequestBody customerRequest: CustomerRequest): ResponseEntity<Any> {
        return try {
            val created = service.createCustomer(Customer.from(customerRequest))
            status(CREATED).body(created)
        } catch (e: Exception) {
            status(BAD_REQUEST).body(e.message)
        }
    }
}