package com.example.simplecrm.mapper;

import com.example.simplecrm.dto.CustomerRequestDTO;
import com.example.simplecrm.dto.CustomerResponseDTO;
import com.example.simplecrm.model.Customer;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerMapper {

    private final ModelMapper mapper;

    public Customer toEntity(CustomerRequestDTO customerRequestDTO) {
        return mapper.map(customerRequestDTO, Customer.class);
    }

    public CustomerResponseDTO toDTO(Customer customer) {
        return mapper.map(customer, CustomerResponseDTO.class);
    }
}
