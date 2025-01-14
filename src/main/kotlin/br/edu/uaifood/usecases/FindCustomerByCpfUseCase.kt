package br.edu.uaifood.usecases

import br.edu.uaifood.ports.inbound.api.customer.dto.CustomerResponse

interface FindCustomerByCpfUseCase {
    fun execute(cpf: String): CustomerResponse
}