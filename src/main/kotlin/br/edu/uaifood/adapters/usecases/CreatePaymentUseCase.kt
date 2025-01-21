package br.edu.uaifood.adapters.usecases

import Payment
import br.edu.uaifood.domain.entities.Customer
import br.edu.uaifood.ports.inbound.api.customer.dto.CustomerResponse
import br.edu.uaifood.ports.inbound.api.payment.dto.PaymentResponse

interface CreatePaymentUseCase {
    fun execute(payment: Payment, orderId: Long): PaymentResponse
}