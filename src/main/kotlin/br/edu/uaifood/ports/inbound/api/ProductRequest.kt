package br.edu.uaifood.ports.inbound.api

class ProductRequest(
    val name: String,
    val description: String,
    val price: Double,
    val category: String
)