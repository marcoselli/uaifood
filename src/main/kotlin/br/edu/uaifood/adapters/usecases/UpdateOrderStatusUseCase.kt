package br.edu.uaifood.adapters.usecases

interface UpdateOrderStatusUseCase {
    fun execute(orderId: Long)
}