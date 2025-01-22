package br.edu.uaifood.usecases

import br.edu.uaifood.adapters.repositories.CustomerRepository
import br.edu.uaifood.domain.entities.Customer
import br.edu.uaifood.domain.entities.CustomerStatus.ACTIVE
import br.edu.uaifood.ports.outbound.repository.customer.CustomerPersisted
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class FindCustomerByCpfUseCaseImplTest {

    private val customerRepository: CustomerRepository = mockk()
    private val findCustomerByCpfUseCaseImpl = FindCustomerByCpfUseCaseImpl(customerRepository)

    @Test
    fun `should find a customer by cpf successfully`() {
        //given
        val customer = Customer("Name Surname", "910.933.630-37", "name.surname@gmail.com", ACTIVE)
        val customerPersisted = CustomerPersisted.from(customer)
        every { customerRepository.findByCpf("910.933.630-37") } returns customerPersisted

        //when
        val result = findCustomerByCpfUseCaseImpl.execute("910.933.630-37");

        //then
        assertThat(result.name).isEqualTo("Name Surname")
        assertThat(result.cpf).isEqualTo("910.933.630-37")
        assertThat(result.email).isEqualTo("name.surname@gmail.com")
        assertThat(result.status).isEqualTo(ACTIVE)
    }

    @Test
    fun `should throw a exception when customer is not found`() {
        //given
        every { customerRepository.findByCpf("910.933.630-37") } returns null

        //when
        val exception = assertThrows<Exception> {
            findCustomerByCpfUseCaseImpl.execute("910.933.630-37");
        }

        //then
        assertThat(exception.message).isEqualTo("404 NOT_FOUND \"Customer for cpf 910.933.630-37 not found\"")
    }
}