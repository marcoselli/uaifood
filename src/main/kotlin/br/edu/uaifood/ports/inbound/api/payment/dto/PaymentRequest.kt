package br.edu.uaifood.ports.inbound.api.payment.dto

data class PaymentRequest(
    var paymentMethod: String,
    var amount: Double,
    var orderId: Long
)