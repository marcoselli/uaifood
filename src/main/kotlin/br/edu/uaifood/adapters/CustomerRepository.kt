package br.edu.uaifood.adapters

import br.edu.uaifood.ports.outbound.repository.customer.CustomerPersistence
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CustomerRepository : JpaRepository<CustomerPersistence, Long> {
    fun findByCpf(cpf: String): Optional<CustomerPersistence>
}