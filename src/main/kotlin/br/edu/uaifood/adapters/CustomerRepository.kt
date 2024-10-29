package br.edu.uaifood.adapters

import br.edu.uaifood.ports.outbound.repository.customer.CustomerPersisted
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomerRepository : JpaRepository<CustomerPersisted, Long>