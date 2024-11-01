package br.edu.uaifood.ports.inbound.api.customer.dto

import br.edu.uaifood.domain.entities.CustomerStatus
import br.edu.uaifood.ports.outbound.repository.customer.CustomerPersisted
import com.fasterxml.jackson.annotation.JsonProperty

data class CustomerResponse(
    var id: Long?,
    var name: String,
    var cpf: String,
    @JsonProperty("e-mail")
    var email: String,
    var status: CustomerStatus
) {
    companion object {

        fun from(customer: CustomerPersisted): CustomerResponse {
            return CustomerResponse(
                name = customer.name,
                cpf = customer.cpf,
                email = customer.email,
                status = customer.status,
                id = customer.id
            )
        }
    }
}