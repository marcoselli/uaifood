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
            paymentId = order.id.toString(),
            amount =  order.products.sumOf { product -> product.price } ,
            qrCode = generateQrCodeUseCase.execute(orderPersisted)
        )
        return runCatching {
            orderPersisted.payment = PaymentPersisted.from(payment)
            orderRepository.save(orderPersisted)
            paymentRepository.save(PaymentPersisted.from(payment))
            OrderResponse.from(orderPersisted)
        }.onSuccess { logger.info("Order created successfully")
        }.onFailure {
            logger.info("Fail to create order: ${it.message}")
            throw  it
        }.getOrThrow()
    }
}