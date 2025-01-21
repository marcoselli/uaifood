package br.edu.uaifood.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class PaymentNotFoundException(paymentId: String)
    : ResponseStatusException(HttpStatus.NOT_FOUND, "Payment id $paymentId not found") {

}
