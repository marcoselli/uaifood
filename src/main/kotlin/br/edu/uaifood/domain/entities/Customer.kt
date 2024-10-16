package br.edu.uaifood.domain.entities

import br.edu.uaifood.ports.dto.customer.CustomerRequest
import br.edu.uaifood.ports.dto.customer.CustomerResponse
import br.edu.uaifood.ports.outbound.repository.customer.CustomerPersistence


class Customer(
    var id: Long? = null,
    var name: String,
    var cpf: String,
    var email: String,
    var status: CustomerStatus
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
        if (!isValidCPF(cpf)) {
            throw IllegalArgumentException("CPF is invalid!")
        }
        if (!isValidEmail(email)) {
            throw IllegalArgumentException("email is invalid!")
        }
    }

    fun toPersistence(): CustomerPersistence {
        return CustomerPersistence(
            id = this.id,
            name = this.name,
            cpf = this.cpf,
            email = this.email,
            status = this.status
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

    private fun isValidCPF(cpf: String): Boolean {
        val cleanCPF = cpf.replace(".", "").replace("-", "")

        if (cleanCPF.length != 11 || cleanCPF.all { it == cleanCPF[0] }) return false

        val dv1 = calculateDigit(cleanCPF, 10)
        val dv2 = calculateDigit(cleanCPF, 11)

        return cleanCPF.endsWith("$dv1$dv2")
    }

    private fun calculateDigit(cpf: String, factor: Int): Int {
        var sum = 0
        for (i in 0 until (factor - 1)) {
            sum += (cpf[i].toString().toInt() * (factor - i))
        }
        val result = 11 - (sum % 11)
        return if (result > 9) 0 else result
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        return email.matches(emailRegex)
    }
}

enum class CustomerStatus { ACTIVE, INACTIVE }