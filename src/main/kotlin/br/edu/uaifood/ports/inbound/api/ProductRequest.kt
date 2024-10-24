package br.edu.uaifood.ports.inbound.api

data class ProductRequest(
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val imageUrl: String
)