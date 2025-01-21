package br.edu.uaifood.ports.inbound.api.payment

import PaymentStatus
import br.edu.uaifood.adapters.usecases.FindOrderByIdUserCase
import br.edu.uaifood.exception.ErrorMessageModel
import br.edu.uaifood.ports.inbound.api.payment.dto.PaymentStatusResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/payments")
class PaymentController(
    private val findOrderByIdUseCase: FindOrderByIdUserCase
) {

    @Operation(summary = "Get payment status", description = "Returns the current status of a payment")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Payment Status Retrieved", content = [Content(schema = Schema(implementation = PaymentStatus::class))]),
            ApiResponse(responseCode = "404", description = "Payment Not Found", content = [Content(schema = Schema(implementation = ErrorMessageModel::class))])
        ]
    )
    @GetMapping("/{paymentId}/status")
    fun getPaymentStatus(@PathVariable paymentId: Long): ResponseEntity<PaymentStatusResponse> =
        findOrderByIdUseCase.execute(paymentId)
            .let { ResponseEntity.ok(it) }

}
