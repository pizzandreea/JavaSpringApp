package com.soupapp.soup.dtos;


import com.soupapp.soup.models.Customer;

public class CustomerResponseDto {
    private Integer Id;
    private String firstName;
    private String lastName;
    private String email;

    public CustomerResponseDto fromCostumer(Customer customer){
        setId(customer.getId());
        setEmail(customer.getEmail());
        setFirstName(customer.getFirstName());
        setLastName(customer.getLastName());

        return this;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
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
