package br.edu.uaifood.domain.services

import br.edu.uaifood.domain.entities.Customer
import br.edu.uaifood.adapters.CustomerRepository
import br.edu.uaifood.adapters.CustomerService
import br.edu.uaifood.ports.inbound.api.CustomerResponse
import br.edu.uaifood.ports.outbound.repository.customer.CustomerPersistence
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CustomerServiceImpl(var repository: CustomerRepository) : CustomerService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun createCustomer(newCustomer: Customer): CustomerResponse {
        logger.info("Creating customer ${newCustomer.name}")
        val persisted = repository.save(CustomerPersistence.from(newCustomer))
        return CustomerResponse.from(persisted)
    }
}