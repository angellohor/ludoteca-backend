package com.ccsw.tutorial.customer;

import com.ccsw.tutorial.customer.model.Customer;
import com.ccsw.tutorial.customer.model.CustomerDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    public void findAllShouldReturnAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        customers.add(mock(Customer.class));

        when(customerRepository.findAll()).thenReturn(customers);

        List<Customer> customer = customerService.findAll();

        assertNotNull(customer);
        assertEquals(1, customer.size());
    }

    public static final String CUSTOMER_NAME = "CUSTOMER1";

    @Test
    public void saveNotExistCustomerIdShouldInsert() throws Exception {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setName(CUSTOMER_NAME);

        ArgumentCaptor<Customer> customer = ArgumentCaptor.forClass(Customer.class);

        customerService.save(null, customerDto);
        verify(customerRepository).save(customer.capture());

        assertEquals(CUSTOMER_NAME, customer.getValue().getName());

    }

    public static final Long EXISTS_CUSTOMER_ID = 1L;

    @Test
    public void saveExistsCustomerIdShouldUpdate() throws Exception {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setName(CUSTOMER_NAME);

        Customer customer = mock(Customer.class);
        when(customerRepository.findById(EXISTS_CUSTOMER_ID)).thenReturn(Optional.of(customer));

        customerService.save(EXISTS_CUSTOMER_ID, customerDto);
        verify(customerRepository).save(customer);
    }

    @Test
    public void saveWithExistsNameShouldThrowException() {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setName(CUSTOMER_NAME);

        when(customerRepository.existsByName(CUSTOMER_NAME)).thenReturn(true);

        Exception exception = assertThrows(Exception.class, () -> {
            customerService.save(null, customerDto);
        });

        assertEquals("This customer name already exists", exception.getMessage());

    }

    @Test
    public void deleteExistsCustomerIdShouldDelete() throws Exception {
        Customer customer = mock(Customer.class);
        when(customerRepository.findById(EXISTS_CUSTOMER_ID)).thenReturn(Optional.of(customer));

        customerService.delete(EXISTS_CUSTOMER_ID);

        verify(customerRepository).deleteById(EXISTS_CUSTOMER_ID);
    }

    public static final Long NOT_EXISTS_CUSTOMER_ID = 0L;

    @Test
    public void getExistsCustomerIdShouldReturnCustomer() {

        Customer customer = mock(Customer.class);
        when(customer.getId()).thenReturn(EXISTS_CUSTOMER_ID);
        when(customerRepository.findById(EXISTS_CUSTOMER_ID)).thenReturn(Optional.of(customer));

        Customer customerResponse = customerService.get(EXISTS_CUSTOMER_ID);

        assertNotNull(customerResponse);
        assertEquals(EXISTS_CUSTOMER_ID, customer.getId());
    }

    @Test
    public void getNotExistsCustomerIdShouldReturnNull() {

        when(customerRepository.findById(NOT_EXISTS_CUSTOMER_ID)).thenReturn(Optional.empty());

        Customer customer = customerService.get(NOT_EXISTS_CUSTOMER_ID);

        assertNull(customer);
    }
}
