package br.edu.uaifood.usecases.impl

import br.edu.uaifood.adapters.CustomerRepository
import br.edu.uaifood.domain.entities.Customer
import br.edu.uaifood.ports.inbound.api.customer.dto.CustomerResponse
import br.edu.uaifood.ports.outbound.repository.customer.CustomerPersisted
import br.edu.uaifood.usecases.CreateCustomerUseCase
import org.slf4j.LoggerFactory

class CreateCustomerUseCaseImpl(private val repository: CustomerRepository): CreateCustomerUseCase {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun execute(customer: Customer): CustomerResponse {
        logger.info("Creating customer ${customer.name}")
        return runCatching {
            repository.save(CustomerPersisted.from(customer))
                .let { CustomerResponse.from(it) }
        }.onSuccess { logger.info("Customer ${customer.name} created successfully")
        }.onFailure { logger.info("Fail to create Customer ${customer.name}: ${it.message}")
        }.getOrThrow()
    }
}