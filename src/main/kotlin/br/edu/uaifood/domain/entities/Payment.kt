import br.edu.uaifood.domain.entities.Order
import br.edu.uaifood.domain.entities.OrderStatus
import br.edu.uaifood.domain.entities.Product
import br.edu.uaifood.ports.outbound.repository.payment.PaymentPersisted
import java.time.LocalDateTime

data class Payment(
    val order: Order?,
    val status: PaymentStatus,
    val paymentId: String?,
    val method: String?,
    val amount: Double,
    val qrCode: String?
) {
    fun isApproved() = status == PaymentStatus.APPROVED

    companion object {
        fun from(paymentPersisted: PaymentPersisted): Payment =
            Payment(
                order = Order.from(paymentPersisted.order),
                status = paymentPersisted.status,
                paymentId = paymentPersisted.paymentId,
                method = paymentPersisted.paymentMethod,
                amount = paymentPersisted.amount,
                qrCode = paymentPersisted.qrCode
            )
    }
}

enum class PaymentStatus {
    PENDING,
    APPROVED,
    DECLINED,
    CANCELLED
}