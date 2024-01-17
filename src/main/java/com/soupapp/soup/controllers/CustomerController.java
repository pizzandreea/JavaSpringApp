package com.soupapp.soup.controllers;

import com.soupapp.soup.dtos.CustomerResponseDto;
import com.soupapp.soup.dtos.CustomerRequestDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.soupapp.soup.services.CustomerService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping( "/api")
public class CustomerController  {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/getAllCustomers")
    public ResponseEntity<List<CustomerResponseDto>> getAll(){
        try{
            List<CustomerResponseDto> customerList = new ArrayList<CustomerResponseDto>();
            customerList = customerService.getAll();
            if(customerList.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(customerList, HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/createCustomer")
    public ResponseEntity<CustomerResponseDto> create(@RequestBody CustomerRequestDto request) throws URISyntaxException {
        var response = customerService.create(request);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(new URI(String.format("/customers/%s",response.getId())));
        return new ResponseEntity<CustomerResponseDto>(response, responseHeaders, HttpStatus.CREATED);
    }
}
