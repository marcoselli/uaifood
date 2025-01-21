package br.edu.uaifood.ports.inbound.api.payment.dto

import PaymentStatus

data class PaymentRequest(
    val status: PaymentStatus
)