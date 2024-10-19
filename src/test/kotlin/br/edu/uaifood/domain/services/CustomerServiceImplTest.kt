package br.edu.uaifood.domain.services

import br.edu.uaifood.domain.entities.Customer
import br.edu.uaifood.adapters.CustomerRepository
import br.edu.uaifood.domain.entities.CustomerStatus.ACTIVE
import br.edu.uaifood.ports.outbound.repository.customer.CustomerPersistence
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import kotlin.test.Test

class CustomerServiceImplTest {
    private val customerRepository: CustomerRepository = mockk()
    private val customerService = CustomerServiceImpl(customerRepository)

    @Test
    fun whenPostCustomer_thenReturnCreatedCustomer() {
        //given
        val newCustomer = Customer("Name Surname", "910.933.630-37", "name.surname@gmail.com", ACTIVE)
        val newCustomerPersistence = CustomerPersistence.from(newCustomer)
        every { customerRepository.save(newCustomerPersistence) } returns newCustomerPersistence;

        //when
        val result = customerService.createCustomer(newCustomer);

        //then
        assertThat(result.name).isEqualTo("Name Surname")
        assertThat(result.cpf).isEqualTo("910.933.630-37")
        assertThat(result.email).isEqualTo("name.surname@gmail.com")
        assertThat(result.status).isEqualTo(ACTIVE)
    }
}
