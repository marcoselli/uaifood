package br.edu.uaifood.adapters

import br.edu.uaifood.ports.outbound.repository.customer.CustomerPersistence
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomerRepository : JpaRepository<CustomerPersistence, Long>