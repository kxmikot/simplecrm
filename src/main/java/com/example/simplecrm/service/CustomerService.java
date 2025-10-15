package com.example.simplecrm.service;

import com.example.simplecrm.dto.CustomerRequestDTO;
import com.example.simplecrm.dto.CustomerResponseDTO;
import com.example.simplecrm.exception.CustomerNotFoundException;
import com.example.simplecrm.mapper.CustomerMapper;
import com.example.simplecrm.model.Customer;
import com.example.simplecrm.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    public CustomerResponseDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
        return customerMapper.toDTO(customer);
    }

    public CustomerResponseDTO createCustomer(CustomerRequestDTO dto) {
        Customer customer = customerMapper.toEntity(dto);
        Customer saved = customerRepository.save(customer);
        return customerMapper.toDTO(saved);
    }

    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
        customerRepository.delete(customer);
    }

    public CustomerResponseDTO updateCustomer(CustomerRequestDTO dto, Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
        customer.setName(dto.getName());
        customer.setEmail(dto.getEmail());
        customer.setPhone(dto.getPhone());

        customerRepository.save(customer);
        return customerMapper.toDTO(customer);
    }

    public List<CustomerResponseDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customerMapper::toDTO)
                .toList();
    }
}
