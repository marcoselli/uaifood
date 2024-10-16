package br.edu.uaifood.ports.inbound.api

import br.edu.uaifood.domain.entities.Customer
import br.edu.uaifood.domain.services.CustomerService
import br.edu.uaifood.ports.dto.customer.CustomerRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/customers")
class CustomerController(var service: CustomerService) {

    @PostMapping("/create")
    fun createCustomer(@RequestBody customerRequest: CustomerRequest): ResponseEntity<Any> {
        return try {
            val newCustomer = Customer(customerRequest)
            val created = service.createCustomer(newCustomer)
            ResponseEntity.ok(created.toResponse())
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.message)
        }
    }
}