package br.edu.uaifood.adapters

import br.edu.uaifood.domain.entities.Product
import br.edu.uaifood.ports.inbound.api.ProductResponse

interface ProductService {

    fun insertIntoMenu(product: Product): ProductResponse

}