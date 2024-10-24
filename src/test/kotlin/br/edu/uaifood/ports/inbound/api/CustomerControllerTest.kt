package br.edu.uaifood.ports.inbound.api

import br.edu.uaifood.adapters.CustomerService
import br.edu.uaifood.domain.entities.Customer
import br.edu.uaifood.ports.outbound.repository.customer.CustomerPersistence
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc

import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@AutoConfigureMockMvc
@SpringBootTest
class CustomerControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
) {

    @RelaxedMockK
    lateinit var customerService: CustomerService

    @Test
    fun whenPostRequestCustomer_thenReturnsStatus200() {
        val customerRequest = CustomerRequest(
            name = "Name Surname",
            cpf = "910.933.630-37",
            email = "name.surname@gmail.com"
        )

        val newCustomer = Customer.from(customerRequest)
        val customerPersistence = CustomerPersistence.from(newCustomer)

        every { customerService.createCustomer(newCustomer) } returns CustomerResponse.from(customerPersistence)

        mockMvc.perform(
            post("/v1/customers/create").content(
                "{\n" +
                        "   \"name\":\"Name Surname\",\n" +
                        "   \"cpf\":\"910.933.630-37\",\n" +
                        "   \"e-mail\":\"name.surname@gmail.com\"\n" +
                        "}"
            )
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Name Surname"))
            .andExpect(jsonPath("$.cpf").value("91093363037"))
            .andExpect(jsonPath("$.e-mail").value("name.surname@gmail.com"))
            .andExpect(jsonPath("$.status").value("ACTIVE"))
    }

    @Test
    fun whenPostRequestCustomerWithInvalidCPf_thenReturnsStatus400() {

        mockMvc.perform(
            post("/v1/customers/create").content(
                "{\n" +
                        "   \"name\":\"Name Surname\",\n" +
                        "   \"cpf\":\"111.222.333-44\",\n" +
                        "   \"e-mail\":\"name.surname@gmail.com\"\n" +
                        "}"
            )
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().string("Invalid CPF"))
    }

    @Test
    fun whenPostRequestCustomerWithInvalidEmail_thenReturnsStatus400() {

        mockMvc.perform(
            post("/v1/customers/create").content(
                "{\n" +
                        "   \"name\":\"Name Surname\",\n" +
                        "   \"cpf\":\"910.933.630-37\",\n" +
                        "   \"e-mail\":\"name.surnameinvalid.com\"\n" +
                        "}"
            )
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().string("Invalid e-mail"))
    }
}