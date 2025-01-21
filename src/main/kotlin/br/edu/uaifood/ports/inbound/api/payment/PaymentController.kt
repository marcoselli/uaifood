package br.edu.uaifood.ports.inbound.api.payment

import br.edu.uaifood.adapters.ProcessPaymentOrderFlowUseCase
import br.edu.uaifood.exception.ErrorMessageModel
import br.edu.uaifood.ports.inbound.api.payment.dto.PaymentRequest
import br.edu.uaifood.ports.inbound.api.product.dto.ProductResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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

    @Operation(summary = "Webhook to process order payment", description = "Returns 200 if successful")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Order payment processed successfully", content = [Content(schema = Schema(implementation = ProductResponse::class))]),
            ApiResponse(responseCode = "404", description = "Order payment not found", content = [Content(schema = Schema(implementation = ErrorMessageModel::class))]),
        ]
    )
    @PostMapping("/{id}/consume")
    fun processPaymentOrder(@PathVariable("id") id: String, @RequestBody paymentRequest: PaymentRequest): ResponseEntity<Void>  =
        processPaymentOrderFlowUseCase.execute(id, paymentRequest.status)
            .let { ResponseEntity.status(HttpStatus.OK).build() }
}