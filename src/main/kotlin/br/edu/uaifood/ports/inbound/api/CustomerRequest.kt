package br.edu.uaifood.ports.inbound.api

import com.fasterxml.jackson.annotation.JsonProperty

data class CustomerRequest(
    @JsonProperty("name")
    var name: String,
    @JsonProperty("cpf")
    var cpf: String,
    @JsonProperty("e-mail")
    var email: String
)
