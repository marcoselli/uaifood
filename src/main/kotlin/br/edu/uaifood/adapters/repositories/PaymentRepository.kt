package br.edu.uaifood.adapters.repositories

import br.edu.uaifood.ports.outbound.repository.payment.PaymentPersisted
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PaymentRepository : JpaRepository<PaymentPersisted, Long> {
    fun findByPaymentId(paymentId: String): PaymentPersisted?
}
