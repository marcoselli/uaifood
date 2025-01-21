import br.edu.uaifood.domain.entities.Order
import br.edu.uaifood.domain.entities.OrderStatus
import br.edu.uaifood.domain.entities.Product
import java.time.LocalDateTime

class Payment(
    var order: Order?,
    var status: PaymentStatus,
    val paymentId: String?,
    val method: String?,
    val amount: Double,
    val qrCode: String?
)

enum class PaymentStatus {
    PENDING,
    APPROVED,
    DECLINED,
    CANCELLED
}