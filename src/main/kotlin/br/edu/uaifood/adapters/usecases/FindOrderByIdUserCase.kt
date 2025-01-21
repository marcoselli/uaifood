package br.edu.uaifood.adapters.usecases

import br.edu.uaifood.ports.inbound.api.payment.dto.PaymentStatusResponse

interface FindOrderByIdUserCase {
    fun execute(orderId: Long) : PaymentStatusResponse
}
