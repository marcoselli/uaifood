package br.edu.uaifood.usecases

import br.edu.uaifood.domain.entities.Customer
import br.edu.uaifood.ports.inbound.api.customer.dto.CustomerResponse

interface CreateCustomerUseCase {
    fun execute(customer: Customer): CustomerResponse
}