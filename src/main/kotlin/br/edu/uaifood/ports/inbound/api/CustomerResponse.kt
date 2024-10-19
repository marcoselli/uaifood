package br.edu.uaifood.ports.inbound.api

import br.edu.uaifood.domain.entities.CustomerStatus
import br.edu.uaifood.ports.outbound.repository.customer.CustomerPersistence
import com.fasterxml.jackson.annotation.JsonProperty

data class CustomerResponse(
    @JsonProperty("id")
    var id: Long?,
    @JsonProperty("name")
    var name: String,
    @JsonProperty("cpf")
    var cpf: String,
    @JsonProperty("e-mail")
    var email: String,
    @JsonProperty("status")
    var status: CustomerStatus
) {
    companion object {

        fun from(customer: CustomerPersistence): CustomerResponse {
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