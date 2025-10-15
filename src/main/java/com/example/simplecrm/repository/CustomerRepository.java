package com.example.simplecrm.repository;

import com.example.simplecrm.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {}