package com.project.spring;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.spring.dto.CustomerRequest;
import com.project.spring.dto.CustomerResponse;
import com.project.spring.model.Customer;
import com.project.spring.repository.CustomerRepository;
import com.project.spring.service.CustomerService;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void testAddCustomer_Success() {
        CustomerRequest request = new CustomerRequest();
        request.setNama("Customer A");
        request.setWallet(5000.0);

        Customer newCustomer = new Customer(UUID.randomUUID(), "123456", "Customer A", 5000.0);
        when(customerRepository.findByNama("Customer A")).thenReturn(null);
        when(customerRepository.save(any(Customer.class))).thenReturn(newCustomer);

        CustomerResponse response = customerService.addCustomer(request);

        assertNotNull(response);
        assertEquals("Customer A", response.getNama());
        assertEquals(5000.0, response.getWallet());
    }

    @Test
    void testAddCustomer_AlreadyExists() {
        CustomerRequest request = new CustomerRequest();
        request.setNama("John Doe");

        when(customerRepository.findByNama(anyString())).thenReturn(new Customer());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            customerService.addCustomer(request);
        });

        assertTrue(exception.getMessage().contains("400 BAD_REQUEST"));
        assertTrue(exception.getMessage().contains("Data already exists"));
        verify(customerRepository, times(1)).findByNama(anyString());
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void testListCustomer() {
        List<Customer> customerList = Arrays.asList(
                new Customer(UUID.randomUUID(), "123456", "Customer A", 5000.0),
                new Customer(UUID.randomUUID(), "789012", "Customer B", 3000.0));
        when(customerRepository.findAll()).thenReturn(customerList);

        List<CustomerResponse> response = customerService.listCustomer();

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("Customer A", response.get(0).getNama());
        assertEquals("Customer B", response.get(1).getNama());
    }

    @Test
    void testGetCustomerById_Success() {
        UUID customerId = UUID.randomUUID();
        Customer existingCustomer = new Customer(customerId, "123456", "Customer A", 5000.0);
        when(customerRepository.findByQrCode("123456")).thenReturn(existingCustomer);

        CustomerResponse response = customerService.getCustomerById("123456");

        assertNotNull(response);
        assertEquals("Customer A", response.getNama());
        assertEquals(5000.0, response.getWallet());
    }

    @Test
    void testGetCustomerById_NotFound() {
        String id = UUID.randomUUID().toString();

        when(customerRepository.findByQrCode(anyString())).thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            customerService.getCustomerById(id);
        });

        assertTrue(exception.getMessage().contains("404 NOT_FOUND"));
        assertTrue(exception.getMessage().contains("Data Not Found"));
        verify(customerRepository, times(1)).findByQrCode(anyString());
    }

    @Test
    void testUpdateCustomer_Success() {
        UUID customerId = UUID.randomUUID();
        Customer existingCustomer = new Customer(customerId, "123456", "Customer A", 5000.0);

        CustomerRequest updateRequest = new CustomerRequest();
        updateRequest.setNama("Updated Customer A");
        updateRequest.setWallet(7000.0);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(existingCustomer)).thenReturn(existingCustomer);

        CustomerResponse response = customerService.updateCustomer(customerId, updateRequest);

        assertNotNull(response);
        assertEquals("Updated Customer A", response.getNama());
        assertEquals(7000.0, response.getWallet());
    }

    @Test
    void testUpdateCustomer_NotFound() {
        UUID id = UUID.randomUUID();
        CustomerRequest request = new CustomerRequest();

        when(customerRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            customerService.updateCustomer(id, request);
        });

        assertTrue(exception.getMessage().contains("404 NOT_FOUND"));
        assertTrue(exception.getMessage().contains("Data Not Found"));
        verify(customerRepository, times(1)).findById(any(UUID.class));
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void testDeleteCustomer_Success() {
        UUID customerId = UUID.randomUUID();
        Customer existingCustomer = new Customer(customerId, "123456", "Customer A", 5000.0);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
        doNothing().when(customerRepository).delete(existingCustomer);

        assertDoesNotThrow(() -> customerService.deleteCustomer(customerId));

        verify(customerRepository, times(1)).delete(existingCustomer);
    }

    @Test
    void testDeleteCustomer_NotFound() {
        UUID id = UUID.randomUUID();

        when(customerRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            customerService.deleteCustomer(id);
        });

        assertTrue(exception.getMessage().contains("404 NOT_FOUND"));
        assertTrue(exception.getMessage().contains("Data Not Found"));
        verify(customerRepository, times(1)).findById(any(UUID.class));
        verify(customerRepository, never()).delete(any(Customer.class));
    }
}
