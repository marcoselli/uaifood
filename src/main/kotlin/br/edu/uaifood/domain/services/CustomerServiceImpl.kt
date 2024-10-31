package br.edu.uaifood.domain.services

import br.edu.uaifood.domain.entities.Customer
import br.edu.uaifood.adapters.CustomerRepository
import br.edu.uaifood.adapters.CustomerService
import br.edu.uaifood.ports.inbound.api.customer.dto.CustomerResponse
import br.edu.uaifood.ports.outbound.repository.customer.CustomerPersisted
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CustomerServiceImpl(private val repository: CustomerRepository) : CustomerService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun createCustomer(customer: Customer): CustomerResponse {
        logger.info("Creating customer ${customer.name}")
        return runCatching {
            repository.save(CustomerPersisted.from(customer))
                .let { CustomerResponse.from(it) }
        }.onSuccess { logger.info("Customer ${customer.name} created successfully")
        }.onFailure { logger.info("Fail to create Customer ${customer.name}: ${it.message}")
        }.getOrThrow()
    }

    override fun findCustomerByCpf(cpf: String): CustomerResponse {
        logger.info("Getting customer by cpf")
        return runCatching {
            repository.findByCpf(cpf).let {
                if (it.isPresent) {
                    CustomerResponse.from(it.get())
                } else {
                    throw Exception("Customer Not Found")
                }
            }
        }.onFailure { logger.info("Fail to find Customer: ${it.message}")
        }.getOrThrow()

    }
}