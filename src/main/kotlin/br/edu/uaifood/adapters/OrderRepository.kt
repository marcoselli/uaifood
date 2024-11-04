package br.edu.uaifood.adapters

import br.edu.uaifood.ports.outbound.repository.order.OrderPersisted
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository : JpaRepository<OrderPersisted, Long>