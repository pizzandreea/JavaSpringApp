package com.soupapp.soup.dtos;


import com.soupapp.soup.models.Customer;

public class CustomerRequestDto {
    private String firstName;
    private String lastName;
    private String email;

    public Customer toCustomer (Customer customer){
        customer.setFirstName(this.getFirstName());
        customer.setLastName(this.getLastName());
        customer.setEmail(this.getEmail());

        return customer;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
