package br.edu.uaifood.ports.inbound.api.product.dto

data class UpsertProductRequest(
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val imageUrl: String
)