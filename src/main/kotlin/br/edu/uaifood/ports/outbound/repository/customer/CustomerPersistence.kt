package br.edu.uaifood.ports.outbound.repository.customer

import br.edu.uaifood.domain.entities.Customer
import br.edu.uaifood.domain.entities.CustomerStatus

import jakarta.persistence.Id
import jakarta.persistence.*

@Entity(name = "customer")
data class CustomerPersistence(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,
    var name: String,
    var cpf: String,
    var email: String,
    @Enumerated(EnumType.STRING)
    var status: CustomerStatus
) {
    companion object {
        fun from(customer: Customer): CustomerPersistence {
            return CustomerPersistence(
                id = null,
                name = customer.name,
                cpf = customer.cpf,
                email = customer.email,
                status = customer.status
            )
        }
    }
}
