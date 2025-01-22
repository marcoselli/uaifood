package br.edu.uaifood.ports.inbound.api.payment.dto

import Payment
import PaymentStatus
import br.edu.uaifood.ports.inbound.api.order.dto.OrderResponse
import br.edu.uaifood.ports.inbound.api.product.dto.ProductResponse
import br.edu.uaifood.ports.outbound.repository.order.OrderPersisted
import br.edu.uaifood.ports.outbound.repository.payment.PaymentPersisted

data class PaymentResponse(
    var id: Long? = null,
    var paymentId: String?,
    var amount: Double,
    var status: PaymentStatus,
    var qrCode: String?,
    var orderId: Long? = null
) {
    companion object {

        fun from(paymentPersisted: PaymentPersisted): PaymentResponse =
            PaymentResponse(
                id = paymentPersisted.id,
                paymentId = paymentPersisted.paymentId,
                amount = paymentPersisted.amount,
                status = paymentPersisted.status,
                qrCode = paymentPersisted.qrCode,
                orderId = paymentPersisted.order!!.id,
            )

        fun from(payment: Payment): PaymentResponse =
            PaymentResponse(
                paymentId = payment.paymentId,
                amount = payment.amount,
                status = payment.status,
                qrCode =  payment.qrCode
            )
    }
}