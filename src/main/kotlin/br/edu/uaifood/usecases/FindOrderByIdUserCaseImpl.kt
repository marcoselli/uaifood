package br.edu.uaifood.usecases

import br.edu.uaifood.adapters.repositories.PaymentRepository
import br.edu.uaifood.adapters.usecases.FindOrderByIdUserCase
import br.edu.uaifood.exception.PaymentNotFoundException
import br.edu.uaifood.ports.inbound.api.payment.dto.PaymentStatusResponse
import org.springframework.stereotype.Service

@Service
class FindOrderByIdUseCaseImpl(
    private val paymentRepository: PaymentRepository
) : FindOrderByIdUserCase {

    override fun execute(orderId: Long): PaymentStatusResponse {
        val paymentPersisted = paymentRepository.findById(orderId)
            .orElseThrow { PaymentNotFoundException(orderId) }

        return PaymentStatusResponse(
            productId = paymentPersisted.id ?: throw IllegalStateException("Payment ID cannot be null"),
            status = paymentPersisted.status
        )
    }
}
