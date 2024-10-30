package br.edu.uaifood.adapters

import br.edu.uaifood.domain.entities.Customer
import br.edu.uaifood.ports.inbound.api.customer.dto.CustomerResponse

interface CustomerService {
    fun createCustomer(newCustomer: Customer): CustomerResponse
}