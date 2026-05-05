package com.ccsw.tutorial.customer;

import com.ccsw.tutorial.customer.model.Customer;
import com.ccsw.tutorial.customer.model.CustomerDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    CustomerRepository customerRepository;

    @Override
    public Customer get(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    @Override
    public List<Customer> findAll() {
        return (List<Customer>) customerRepository.findAll();
    }

    @Override
    public void save(Long id, CustomerDto customerDto) throws Exception {
        if (customerRepository.existsByName(customerDto.getName())) {
            throw new Exception("This customer name already exists");
        }
        Customer customer;
        if (id == null) {
            customer = new Customer();
        } else {
            customer = get(id);
        }
        customer.setName(customerDto.getName());
        customerRepository.save(customer);

    }

    @Override
    public void delete(Long id) throws Exception {
        if (get(id) == null) {
            throw new Exception("Not Exists");
        }
        customerRepository.deleteById(id);

    }

}
