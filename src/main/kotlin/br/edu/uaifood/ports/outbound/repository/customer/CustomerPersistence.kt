package br.edu.uaifood.ports.outbound.repository.customer

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity(name = "customer")
data class CustomerPersistence(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,
    var name: String,
    var cpf: String,
    var email: String,
    var status: String
)