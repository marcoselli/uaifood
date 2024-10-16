package br.edu.uaifood.ports.inbound.api

import br.edu.uaifood.domain.entities.Customer
import br.edu.uaifood.domain.services.CustomerService
import br.edu.uaifood.ports.dto.customer.CustomerRequest
import com.fasterxml.jackson.databind.ObjectMapper
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
    val mapper: ObjectMapper
) {

    @RelaxedMockK
    lateinit var customerService: CustomerService


    @Test
    fun whenPostRequestCustomer_thenReturnsStatus200() {
        val customerRequest = CustomerRequest(
            name = "name",
            cpf = "910.933.630-37",
            email = "name.surname@gmail.com"
        )

        val newCustomer = Customer(customerRequest)

        every { customerService.createCustomer(newCustomer) } returns newCustomer;

        mockMvc.perform(
            post("/customers/create").content(mapper.writeValueAsString(customerRequest))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("name"))
            .andExpect(jsonPath("$.cpf").value("910.933.630-37"))
            .andExpect(jsonPath("$.e-mail").value("name.surname@gmail.com"))
            .andExpect(jsonPath("$.status").value("ACTIVE"))
    }
}