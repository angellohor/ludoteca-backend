package com.ccsw.tutorial.customer;

import com.ccsw.tutorial.customer.model.CustomerDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CustomerIT {
    public static final String LOCALHOST = "http://localhost:";
    public static final String SERVICE_PATH = "/customer";

    public static final Long NEW_CUSTOMER_ID = 4L;
    public static final String NEW_CUSTOMER_NAME = "Nuevo Cliente";
    public static final Long EXISTS_CUSTOMER_ID = 1L;
    public static final Long NOT_EXISTS_CUSTOMER_ID = 0L;
    public static final String EXISTING_CUSTOMER_NAME = "Paco";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    ParameterizedTypeReference<List<CustomerDto>> responseType = new ParameterizedTypeReference<List<CustomerDto>>() {
    };

    @Test
    public void findAllShouldReturnAllCustomers() {
        ResponseEntity<List<CustomerDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.GET, null, responseType);

        assertNotNull(response);
        assertEquals(3, response.getBody().size());
    }

    @Test
    public void saveWithoutIdShouldCreateNewCustomer() {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setName(NEW_CUSTOMER_NAME);

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(customerDto), Void.class);

        ResponseEntity<List<CustomerDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.GET, null, responseType);

        assertNotNull(response);
        assertEquals(4, response.getBody().size());
    }

    @Test
    public void saveWithExistsIdShouldUpdateCustomer() {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setName(NEW_CUSTOMER_NAME);

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + EXISTS_CUSTOMER_ID, HttpMethod.PUT, new HttpEntity<>(customerDto), Void.class);

        ResponseEntity<List<CustomerDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.GET, null, responseType);

        assertNotNull(response);
        assertEquals(3, response.getBody().size());

        CustomerDto customer = response.getBody().stream().filter(c -> c.getId().equals(EXISTS_CUSTOMER_ID)).findFirst().orElse(null);
        assertNotNull(customer);
        assertEquals(NEW_CUSTOMER_NAME, customer.getName());
    }

    @Test
    public void saveWithExistingNameShouldThrowException() {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setName(EXISTING_CUSTOMER_NAME);

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(customerDto), Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    private static final Long DELETE_CUSTOMER_ID = 3L;

    @Test
    public void deleteWithExistsIdShouldDeleteCustomer() {
        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + DELETE_CUSTOMER_ID, HttpMethod.DELETE, null, Void.class);

        ResponseEntity<List<CustomerDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.GET, null, responseType);

        assertNotNull(response);
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void deleteWithNotExistsIdShouldThrowException() {
        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + NOT_EXISTS_CUSTOMER_ID, HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
