package com.soupapp.soup.repositories;

import com.soupapp.soup.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
// operatiile crud din jpa
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
