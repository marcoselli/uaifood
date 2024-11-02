package br.edu.uaifood.ports.inbound.api.order.dto

import br.edu.uaifood.ports.inbound.api.product.dto.UpsertProductRequest

class OrderRequest(
    var products: List<UpsertProductRequest> = emptyList()
)