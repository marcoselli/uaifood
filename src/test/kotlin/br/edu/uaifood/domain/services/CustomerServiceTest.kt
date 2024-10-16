package br.edu.uaifood.domain.services

import br.edu.uaifood.domain.entities.Customer
import br.edu.uaifood.domain.entities.CustomerStatus
import br.edu.uaifood.ports.outbound.repository.customer.CustomerRepository
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import kotlin.test.Test

class CustomerServiceTest {
    private val customerRepository: CustomerRepository = mockk()
    private val customerService = CustomerService(customerRepository)

    @Test
    fun whenPostCustomer_thenReturnCreatedCustomer() {
        //given
        val newCustomer = Customer(null, "name", "910.933.630-37", "name.surname@gmail.com", CustomerStatus.ACTIVE)
        val newCustomerPersistence = newCustomer.toPersistence()
        every { customerRepository.save(newCustomerPersistence) } returns newCustomerPersistence;

        //when
        val result = customerService.createCustomer(newCustomer);

        //then
        assertThat(result.name).isEqualTo("name")
        assertThat(result.cpf).isEqualTo("910.933.630-37")
        assertThat(result.email).isEqualTo("name.surname@gmail.com")
        assertThat(result.status).isEqualTo(CustomerStatus.ACTIVE)
    }
}
