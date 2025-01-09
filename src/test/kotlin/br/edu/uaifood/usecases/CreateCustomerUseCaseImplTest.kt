package br.edu.uaifood.usecases

import br.edu.uaifood.adapters.CustomerRepository
import br.edu.uaifood.domain.entities.Customer
import br.edu.uaifood.domain.entities.CustomerStatus.ACTIVE
import br.edu.uaifood.ports.outbound.repository.customer.CustomerPersisted
import br.edu.uaifood.usecases.impl.CreateCustomerUseCaseImpl
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import kotlin.test.Test

class CreateCustomerUseCaseImplTest {

    private val customerRepository: CustomerRepository = mockk()
    private val createCustomerUseCaseImpl = CreateCustomerUseCaseImpl(customerRepository)

    @Test
    fun `should save a customer successfully`() {
        //given
        val customer = Customer("Name Surname", "910.933.630-37", "name.surname@gmail.com", ACTIVE)
        val customerPersisted = CustomerPersisted.from(customer)
        every { customerRepository.save(customerPersisted) } returns customerPersisted;

        //when
        val result = createCustomerUseCaseImpl.execute(customer);

        //then
        assertThat(result.name).isEqualTo("Name Surname")
        assertThat(result.cpf).isEqualTo("910.933.630-37")
        assertThat(result.email).isEqualTo("name.surname@gmail.com")
        assertThat(result.status).isEqualTo(ACTIVE)
    }
}