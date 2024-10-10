package br.edu.uaifood.domain.entities

import br.edu.uaifood.ports.dto.customer.CustomerRequest
import br.edu.uaifood.ports.dto.customer.CustomerResponse
import br.edu.uaifood.ports.outbound.repository.customer.CustomerPersistence


class Customer(
    private var id: Long? = null,
    private var name: String,
    private var cpf: String,
    private var email: String,
    private var status: CustomerStatus
) {

    constructor(customerRequest: CustomerRequest) :
            this(
                name = customerRequest.name,
                cpf = customerRequest.cpf,
                email = customerRequest.email,
                status = CustomerStatus.ACTIVE
            )

    constructor(customerPersistence: CustomerPersistence) :
            this(
                id = customerPersistence.id,
                name = customerPersistence.name,
                cpf = customerPersistence.cpf,
                email = customerPersistence.email,
                status = CustomerStatus.ACTIVE
            )

    init {
        if (cpf.isEmpty()) {
            throw IllegalArgumentException("Surname cannot be empty!")
        }
        if (email.isEmpty()) {
            throw IllegalArgumentException("Surname cannot be empty!")
        }
    }

    fun toPersistence(): CustomerPersistence {
        return CustomerPersistence(
            id = this.id,
            name = this.name,
            cpf = this.cpf,
            email = this.email,
            status = this.status.toString()
        )
    }

    fun toResponse(): CustomerResponse {
        return CustomerResponse(
            id = this.id,
            name = this.name,
            cpf = this.cpf,
            email = this.email,
            status = this.status
        )
    }

}


class Cpf
//TODO validation

class Email
//TODO validation

enum class CustomerStatus { ACTIVE, INACTIVE }