package br.edu.uaifood.domain.entities

import java.time.LocalDateTime

class Order(
    var products: List<Product> = emptyList(),
    var status: OrderStatus,
    var creationDate: LocalDateTime
)

enum class OrderStatus { RECEIVED, IN_PREPARATION, READY, FINISHED }