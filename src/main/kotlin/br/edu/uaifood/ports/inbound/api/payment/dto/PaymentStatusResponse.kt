package br.edu.uaifood.ports.inbound.api.payment.dto

import PaymentStatus

data class PaymentStatusResponse(
    var productId : Long,
    var status: PaymentStatus
)
