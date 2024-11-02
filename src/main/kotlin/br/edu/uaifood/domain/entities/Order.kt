package br.edu.uaifood.domain.entities

class Order (
//    var products: List<Product>,
    var status: OrderStatus
)

enum class OrderStatus { RECEIVED, IN_PREPARATION, READY, FINISHED }