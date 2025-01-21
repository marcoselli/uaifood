package br.edu.uaifood.ports.inbound.api.payment

import br.edu.uaifood.adapters.ProcessPaymentOrderFlowUseCase
import br.edu.uaifood.ports.inbound.api.payment.dto.PaymentRequest
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/payments")
class PaymentController(
    private val processPaymentOrderFlowUseCase: ProcessPaymentOrderFlowUseCase
) {

    @PostMapping("/{/id}/consume")
    fun processPaymentOrder(@PathVariable("id") id: String, @RequestBody paymentRequest: PaymentRequest) =
        processPaymentOrderFlowUseCase.execute(id, paymentRequest.status)
}