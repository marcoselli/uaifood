package br.edu.uaifood.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class PaymentNotFoundException(orderId: Long) :
    ResponseStatusException(HttpStatus.NOT_FOUND, "Payment with order ID $orderId not found")