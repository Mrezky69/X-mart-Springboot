package com.project.spring;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.spring.dto.CustomerRequest;
import com.project.spring.dto.CustomerResponse;
import com.project.spring.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerService customerService;

    @Test
    void testListCustomers() throws Exception {
        CustomerResponse customer = new CustomerResponse();
        customer.setId(UUID.randomUUID());
        customer.setNama("Test Customer");
        customer.setWallet(5000.0);
        Mockito.when(customerService.listCustomer()).thenReturn(Collections.singletonList(customer));

        mockMvc.perform(get("/api/customers/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nama").value("Test Customer"))
                .andExpect(jsonPath("$[0].wallet").value(5000.0));
    }

    @Test
    void testGetCustomerById_NotFound() throws Exception {
        Mockito.when(customerService.getCustomerById(Mockito.anyString())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Data tidak ditemukan."));

        mockMvc.perform(get("/api/customers/invalid-id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetCustomerById_Success() throws Exception {
        CustomerResponse customer = new CustomerResponse();
        UUID id = UUID.randomUUID();
        customer.setId(id);
        customer.setNama("Test Customer");
        customer.setWallet(5000.0);
        Mockito.when(customerService.getCustomerById(id.toString())).thenReturn(customer);

        mockMvc.perform(get("/api/customers/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nama").value("Test Customer"))
                .andExpect(jsonPath("$.wallet").value(5000.0));
    }

    @Test
    void testAddCustomer_Success() throws Exception {
        CustomerRequest request = new CustomerRequest();
        request.setNama("Test Customer");
        request.setWallet(5000.0);

        CustomerResponse response = new CustomerResponse();
        response.setId(UUID.randomUUID());
        response.setNama("Test Customer");
        response.setWallet(5000.0);

        Mockito.when(customerService.addCustomer(Mockito.any(CustomerRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/customers/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nama").value("Test Customer"))
                .andExpect(jsonPath("$.wallet").value(5000.0));
    }

    @Test
    void testUpdateCustomer_Success() throws Exception {
        UUID id = UUID.randomUUID();
        CustomerRequest request = new CustomerRequest();
        request.setNama("Updated Customer");
        request.setWallet(6000.0);

        CustomerResponse response = new CustomerResponse();
        response.setId(id);
        response.setNama("Updated Customer");
        response.setWallet(6000.0);

        Mockito.when(customerService.updateCustomer(Mockito.eq(id), Mockito.any(CustomerRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/customers/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nama").value("Updated Customer"))
                .andExpect(jsonPath("$.wallet").value(6000.0));
    }

    @Test
    void testDeleteCustomer_Success() throws Exception {
        UUID id = UUID.randomUUID();
        Mockito.doNothing().when(customerService).deleteCustomer(id);

        mockMvc.perform(delete("/api/customers/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
