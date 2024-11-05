package br.edu.uaifood.domain.services

import br.edu.uaifood.adapters.CheckoutService
import org.springframework.stereotype.Service

@Service
class CheckoutServiceImpl : CheckoutService {

    // TODO("in the next phases of the project we will offer a MercadoPago QRCode for payment")
    override fun fakeCheckout(): Boolean {
        return true
    }
}