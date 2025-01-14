package br.edu.uaifood.usecases

import br.edu.uaifood.adapters.repositories.CustomerRepository
import br.edu.uaifood.adapters.usecases.FindCustomerByCpfUseCase
import br.edu.uaifood.exception.CustomerNotFoundException
import br.edu.uaifood.ports.inbound.api.customer.dto.CustomerResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class FindCustomerByCpfUseCaseImpl(
    private val repository: CustomerRepository
): FindCustomerByCpfUseCase {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun execute(cpf: String): CustomerResponse {
        logger.info("Getting customer by cpf")
        return runCatching {
            val customer =
                repository.findByCpf(cpf) ?: throw CustomerNotFoundException("Customer for cpf $cpf not found")
            CustomerResponse.from(customer)
        }.onFailure { logger.info("Fail to find Customer: ${it.message}")
        }.getOrThrow()
    }
}