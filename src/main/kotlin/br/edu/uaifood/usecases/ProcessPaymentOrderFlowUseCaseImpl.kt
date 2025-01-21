package br.edu.uaifood.usecases

import Payment
import PaymentStatus
import br.edu.uaifood.adapters.ProcessPaymentOrderFlowUseCase
import br.edu.uaifood.adapters.repositories.OrderRepository
import br.edu.uaifood.adapters.repositories.PaymentRepository
import br.edu.uaifood.domain.entities.Order
import br.edu.uaifood.domain.entities.OrderStatus
import br.edu.uaifood.exception.PaymentNotFound
import br.edu.uaifood.ports.outbound.repository.order.OrderPersisted
import org.springframework.stereotype.Component

@Component
class ProcessPaymentOrderFlowUseCaseImpl(
    private val paymentRepository: PaymentRepository,
    private val orderRepository: OrderRepository
): ProcessPaymentOrderFlowUseCase {

    override fun execute(paymentId: String, paymentStatus: PaymentStatus) {
        val paymentPersisted = paymentRepository.findByPaymentId(paymentId) ?: throw PaymentNotFound()
        paymentRepository.save(paymentPersisted.copy(status = paymentStatus))
        val payment = Payment.from(paymentPersisted)
        if (payment.isApproved())  approveOrder(payment.order!!)
    }

    private fun approveOrder(order: Order) {
        orderRepository.save(OrderPersisted.from(order.copy(status = OrderStatus.READY)))
    }
}