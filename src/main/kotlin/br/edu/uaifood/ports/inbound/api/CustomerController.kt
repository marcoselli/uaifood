package br.edu.uaifood.ports.inbound.api

import br.edu.uaifood.adapters.CustomerService
import br.edu.uaifood.domain.entities.Customer
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
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

    @Operation(summary = "Creates a new customer", description = "Returns 202 if successful")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "202", description = "Customer created"),
            ApiResponse(responseCode = "400", description = "Error creating Customer"),
        ]
    )
    @PostMapping("/create")
    fun createCustomer(@RequestBody customerRequest: CustomerRequest): ResponseEntity<Any> {
        return try {
            val created = service.createCustomer(Customer.from(customerRequest))
            status(CREATED).body(created)
        } catch (e: Exception) {
            status(BAD_REQUEST).body(e.message)
        }
    }
}