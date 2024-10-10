package br.edu.uaifood.ports.dto.customer

import br.edu.uaifood.domain.entities.CustomerStatus
import com.fasterxml.jackson.annotation.JsonProperty

data class CustomerRequest (
    @JsonProperty("name")
    var name: String,
    @JsonProperty("cpf")
    var cpf: String,
    @JsonProperty("e-mail")
    var email: String)

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
)