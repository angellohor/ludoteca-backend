package com.ccsw.tutorial.customer;

import com.ccsw.tutorial.customer.model.Customer;
import com.ccsw.tutorial.customer.model.CustomerDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Customer", description = "API of Customer")
@RestController
@RequestMapping(value = "/customer")
@CrossOrigin(origins = "*")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @Autowired
    ModelMapper modelMapper;

    @Operation(summary = "Find", description = "Method that returns a list of customers")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<CustomerDto> findAll() {
        List<Customer> customers = customerService.findAll();
        return customers.stream().map(e -> modelMapper.map(e, CustomerDto.class)).collect(Collectors.toList());
    }

    @Operation(summary = "Save and Update", description = "Method that saves or updates a customer")
    @RequestMapping(path = { "/{id}", "" }, method = RequestMethod.PUT)
    public void save(@PathVariable(name = "id", required = false) Long id, @RequestBody CustomerDto customerDto) throws Exception {
        customerService.save(id, customerDto);
    }

    @Operation(summary = "Delete", description = "Method that deletes a customer")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable(name = "id") Long id) throws Exception {
        customerService.delete(id);
    }
}
