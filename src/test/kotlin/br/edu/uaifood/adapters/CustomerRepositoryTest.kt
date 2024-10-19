package br.edu.uaifood.adapters

import br.edu.uaifood.domain.entities.Customer
import br.edu.uaifood.domain.entities.CustomerStatus.ACTIVE
import br.edu.uaifood.ports.outbound.repository.customer.CustomerPersistence
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
    fun whenCreateACustomer_thenReturnCreatedCustomer() {
        val newCustomer = Customer("Name Surname", "910.933.630-37", "name.surname@gmail.com", ACTIVE)

        val persisted = entityManager.persist(CustomerPersistence.from(newCustomer))
        entityManager.flush()

        assertThat(persisted.id).isEqualTo(1)
        assertThat(persisted.name).isEqualTo("Name Surname")
        assertThat(persisted.cpf).isEqualTo("910.933.630-37")
        assertThat(persisted.email).isEqualTo("name.surname@gmail.com")
        assertThat(persisted.status).isEqualTo(ACTIVE)
    }
}