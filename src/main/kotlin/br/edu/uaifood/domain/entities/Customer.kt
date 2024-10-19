package br.edu.uaifood.domain.entities

import br.edu.uaifood.ports.inbound.api.CustomerRequest

class Customer(
    var name: String,
    var cpf: String,
    var email: String,
    var status: CustomerStatus
) {
    companion object {
        fun from(request: CustomerRequest) =
            Customer(
                name = request.name,
                cpf = validateCPF(request.cpf),
                email = validateEmail(request.email),
                status = CustomerStatus.ACTIVE
            )

        private fun validateCPF(cpf: String): String {
            val cleanedCPF = cpf.replace(".", "").replace("-", "")

            if (cleanedCPF.length == 11 && validDigit(cleanedCPF)) {
                return cleanedCPF
            } else {
                throw Exception("Invalid CPF")
            }
        }

        private fun validDigit(cleanedCPF: String): Boolean {
            val dv1 = calculateDigit(cleanedCPF, 10)
            val dv2 = calculateDigit(cleanedCPF, 11)

            return cleanedCPF.endsWith("$dv1$dv2")
        }

        private fun calculateDigit(cpf: String, factor: Int): Int {
            var sum = 0
            for (i in 0 until (factor - 1)) {
                sum += (cpf[i].toString().toInt() * (factor - i))
            }
            val result = 11 - (sum % 11)
            return if (result > 9) 0 else result
        }

        private fun validateEmail(email: String): String =
            if (email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex())) email
            else throw Exception("Invalid e-mail")
    }
}

enum class CustomerStatus { ACTIVE, INACTIVE }