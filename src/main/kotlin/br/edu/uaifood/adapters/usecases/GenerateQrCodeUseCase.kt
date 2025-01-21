package br.edu.uaifood.adapters.usecases

import br.edu.uaifood.ports.outbound.repository.order.OrderPersisted

interface GenerateQrCodeUseCase {
    fun execute (orderPersisted: OrderPersisted) : String
}