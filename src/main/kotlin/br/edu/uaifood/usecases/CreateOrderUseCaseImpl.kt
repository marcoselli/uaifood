package br.edu.uaifood.usecases

import Payment
import br.edu.uaifood.adapters.repositories.OrderRepository
import br.edu.uaifood.adapters.repositories.PaymentRepository
import br.edu.uaifood.adapters.usecases.CreateOrderUseCase
import br.edu.uaifood.adapters.usecases.CreatePaymentUseCase
import br.edu.uaifood.adapters.usecases.FindProductsByIdsUseCase
import br.edu.uaifood.adapters.usecases.GenerateQrCodeUseCase
import br.edu.uaifood.domain.entities.Order
import br.edu.uaifood.ports.inbound.api.order.dto.OrderResponse
import br.edu.uaifood.ports.outbound.repository.order.OrderPersisted
import br.edu.uaifood.ports.outbound.repository.payment.PaymentPersisted
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class CreateOrderUseCaseImpl(
    var orderRepository: OrderRepository,
    var findProductsByIdsUseCase: FindProductsByIdsUseCase,
    var paymentRepository: PaymentRepository,
    val generateQrCodeUseCase: GenerateQrCodeUseCase
): CreateOrderUseCase {
    private val logger = LoggerFactory.getLogger(this::class.java)
    override fun execute(order: Order): OrderResponse {
        logger.info("Creating order")

        val managedProducts = findProductsByIdsUseCase.execute(order.products)
        val orderPersisted = OrderPersisted.from(order).apply { products = managedProducts }
        val payment = Payment(
        order = Order.from(orderPersisted),
        status = PaymentStatus.PENDING,
        paymentId = null,
        method = "",
        amount = 100.0,
        qrCode = "");
        orderPersisted.payment =
            PaymentPersisted.from(payment)

        return runCatching {

            orderRepository.save(orderPersisted).also {
                paymentRepository.save(
                    PaymentPersisted.from
                        (Payment(
                        order = Order.from(orderPersisted),
                        status = PaymentStatus.PENDING,
                        paymentId = "123456",
                        method = "",
                        amount = 100.0,
                        qrCode = generateQrCodeUseCase.execute(orderPersisted))))
            }.let { OrderResponse.from(it) }

        }.onSuccess { logger.info("Order created successfully")
        }.onFailure {
            logger.info("Fail to create order: ${it.message}")
            throw  it
        }.getOrThrow()
    }
}