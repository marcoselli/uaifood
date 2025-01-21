package br.edu.uaifood.usecases

import br.edu.uaifood.adapters.repositories.PaymentRepository
import br.edu.uaifood.adapters.usecases.FindPaymentByIdUserCase
import br.edu.uaifood.exception.PaymentNotFoundException
import br.edu.uaifood.ports.inbound.api.payment.dto.PaymentStatusResponse
import org.springframework.stereotype.Service

@Service
class FindPaymentByIdUseCaseImpl(
    private val paymentRepository: PaymentRepository
) : FindPaymentByIdUserCase {

    override fun execute(paymentId: Long): PaymentStatusResponse {
        val paymentPersisted = paymentRepository.findById(paymentId)
            .orElseThrow { PaymentNotFoundException(paymentId) }

        return PaymentStatusResponse(
            productId = paymentPersisted.id ?: throw IllegalStateException("Payment ID cannot be null"),
            status = paymentPersisted.status
        )
    }
}
