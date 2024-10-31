package br.edu.uaifood.ports.inbound.api.customer

import br.edu.uaifood.adapters.CustomerService
import br.edu.uaifood.domain.entities.Customer
import br.edu.uaifood.ports.inbound.api.customer.dto.CustomerRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.*
import org.springframework.web.bind.annotation.*

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

    @Operation(summary = "Get a customer by Cpf", description = "Returns 200 if successful")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Customer"),
            ApiResponse(responseCode = "404", description = "Customer Not Found"),
        ]
    )
    @GetMapping
    fun findCustomerByCpf(@RequestParam cpf: String): ResponseEntity<Any> {
        return try {
            val customer = service.findCustomerByCpf(Customer.validateCpf(cpf))
            status(OK).body(customer)
        } catch (e: Exception) {
            status(NOT_FOUND).body(e.message)
        }
    }
}