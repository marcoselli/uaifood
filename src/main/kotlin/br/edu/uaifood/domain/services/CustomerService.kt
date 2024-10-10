package br.edu.uaifood.domain.services

import br.edu.uaifood.domain.entities.Customer
import br.edu.uaifood.ports.outbound.repository.customer.CustomerRepository
import org.springframework.stereotype.Service

@Service
class CustomerService(var repository: CustomerRepository) {

    fun createCustomer(newCustomer: Customer): Customer {
        val persisted = repository.save(newCustomer.toPersistence())
        return Customer(persisted)
    }
}