package com.ccsw.tutorial.customer;

import com.ccsw.tutorial.customer.model.Customer;
import com.ccsw.tutorial.customer.model.CustomerDto;

import java.util.List;

public interface CustomerService {

    Customer get(Long id);

    List<Customer> findAll();

    void save(Long id, CustomerDto customerDto) throws Exception;

    void delete(Long id) throws Exception;

}
