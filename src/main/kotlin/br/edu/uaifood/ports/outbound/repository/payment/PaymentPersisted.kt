package br.edu.uaifood.ports.outbound.repository.payment

import Payment
import PaymentStatus
import br.edu.uaifood.domain.entities.Order
import br.edu.uaifood.ports.outbound.repository.order.OrderPersisted
import br.edu.uaifood.ports.outbound.repository.product.ProductPersisted
import jakarta.persistence.*

@Entity(name = "payment")
data class PaymentPersisted (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,
    val paymentId: String?, // ID gerado pelo sistema de pagamento (e.g., Mercado Pago)
    @Enumerated(EnumType.STRING)
    var status: PaymentStatus, // Status do pagamento (e.g., PENDING, APPROVED)
    var qrCode: String?, // Método de pagamento (e.g., CREDIT_CARD, PIX)
    val amount: Double, // Valor do pagamento
    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    var order: OrderPersisted? = null // Relacionamento com o pedido
){
    companion object {
        fun from(payment: Payment): PaymentPersisted {
            return PaymentPersisted(
                id = null,
                status = payment.status,
                qrCode = payment.qrCode,
                amount = payment.amount,
                paymentId = payment.paymentId,
                order = null
            )
        }
    }
}