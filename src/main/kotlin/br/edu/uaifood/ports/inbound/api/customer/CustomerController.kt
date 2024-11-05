package br.edu.uaifood.ports.inbound.api.customer

import br.edu.uaifood.adapters.CustomerService
import br.edu.uaifood.domain.entities.Customer
import br.edu.uaifood.exception.ErrorMessageModel
import br.edu.uaifood.ports.inbound.api.customer.dto.CustomerRequest
import br.edu.uaifood.ports.inbound.api.customer.dto.CustomerResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity.*
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/customers")
class CustomerController(var service: CustomerService) {

    @Operation(summary = "Creates a new customer", description = "Returns 201 if successful")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Customer created", content = [Content(schema = Schema(implementation = CustomerResponse::class))]),
            ApiResponse(responseCode = "400", description = "Error creating Customer", content = [Content(schema = Schema(implementation = ErrorMessageModel::class))]),
        ]
    )
    @PostMapping
    fun createCustomer(@RequestBody customerRequest: CustomerRequest) =
        service.createCustomer(Customer.from(customerRequest))
            .let { status(CREATED).body(it) }

    @Operation(summary = "Get a customer by Cpf", description = "Returns 200 if successful")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Customer", content = [Content(schema = Schema(implementation = CustomerResponse::class))]),
            ApiResponse(responseCode = "404", description = "Customer Not Found", content = [Content(schema = Schema(implementation = ErrorMessageModel::class))]),
        ]
    )
    @GetMapping
    fun findCustomerByCpf(@RequestParam cpf: String) =
        service.findCustomerByCpf(Customer.validateCpf(cpf))
            .let { status(OK).body(it) }
}