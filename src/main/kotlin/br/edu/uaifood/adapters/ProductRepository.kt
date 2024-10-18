package br.edu.uaifood.adapters

import br.edu.uaifood.ports.outbound.repository.ProductPersisted
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ProductRepository: JpaRepository<ProductPersisted, UUID>  {
    fun findByName(name: String): List<ProductPersisted>
}