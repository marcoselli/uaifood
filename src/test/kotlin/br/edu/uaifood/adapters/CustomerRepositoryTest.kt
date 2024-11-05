package br.edu.uaifood.adapters

import br.edu.uaifood.domain.entities.Customer
import br.edu.uaifood.domain.entities.CustomerStatus.ACTIVE
import br.edu.uaifood.ports.outbound.repository.customer.CustomerPersisted
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import kotlin.test.Test

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    lateinit var entityManager: TestEntityManager

    @Autowired
    lateinit var customerRepository: CustomerRepository

    @Test
    fun `should save a customer successfully`() {
        //given
        val newCustomer = Customer("Name Surname", "910.933.630-37", "name.surname@gmail.com", ACTIVE)

        //when
        val persisted = customerRepository.save(CustomerPersisted.from(newCustomer))

        //then
        assertThat(persisted.name).isEqualTo("Name Surname")
        assertThat(persisted.cpf).isEqualTo("910.933.630-37")
        assertThat(persisted.email).isEqualTo("name.surname@gmail.com")
        assertThat(persisted.status).isEqualTo(ACTIVE)
    }

    @Test
    fun `should find a customer by cpf successfully`() {
        //given
        val newCustomer = Customer("Name Surname", "910.933.630-37", "name.surname@gmail.com", ACTIVE)
        entityManager.persist(CustomerPersisted.from(newCustomer))

        //when
        val customer = customerRepository.findByCpf("910.933.630-37")

        //then
        assertThat(customer?.name).isEqualTo("Name Surname")
        assertThat(customer?.cpf).isEqualTo("910.933.630-37")
        assertThat(customer?.email).isEqualTo("name.surname@gmail.com")
        assertThat(customer?.status).isEqualTo(ACTIVE)
    }
}