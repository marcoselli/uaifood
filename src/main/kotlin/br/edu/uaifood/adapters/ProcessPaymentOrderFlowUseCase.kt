package br.edu.uaifood.adapters

import PaymentStatus

interface ProcessPaymentOrderFlowUseCase {
    fun execute(paymentId: String, paymentStatus: PaymentStatus)
}
