package com.soupapp.soup.services;


import com.soupapp.soup.dtos.CustomerRequestDto;
import com.soupapp.soup.dtos.CustomerResponseDto;
import com.soupapp.soup.repositories.CustomerRepository;
import com.soupapp.soup.models.Customer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerResponseDto create(CustomerRequestDto request) {
        var customers = customerRepository.findAll();
        if(customers.stream().noneMatch(x -> x.getEmail().equals(request.getEmail()))){
            Customer customer = request.toCustomer(new Customer());
            var createdCustomer = customerRepository.save(customer);
            return new CustomerResponseDto().fromCostumer(createdCustomer);
        }
        return null;
    }

    public List<CustomerResponseDto> getAll(){
        var customers = customerRepository.findAll();
        return customers.stream().map(x -> new CustomerResponseDto().fromCostumer(x))
                .collect(Collectors.toList());
    }
}
