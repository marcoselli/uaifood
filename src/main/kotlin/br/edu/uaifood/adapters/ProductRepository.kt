package br.edu.uaifood.adapters

import br.edu.uaifood.ports.outbound.repository.product.ProductPersisted
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ProductRepository: JpaRepository<ProductPersisted, UUID>  {
    fun findByName(name: String): ProductPersisted?
}