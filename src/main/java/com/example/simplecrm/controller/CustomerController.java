package com.example.simplecrm.controller;

import com.example.simplecrm.dto.CustomerRequestDTO;
import com.example.simplecrm.dto.CustomerResponseDTO;
import com.example.simplecrm.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customers", description = "Operations with Customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "Add new customer")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponseDTO createCustomer(@Valid @RequestBody CustomerRequestDTO dto) {
        return customerService.createCustomer(dto);
    }

    @Operation(summary = "Get customer by ID")
    @GetMapping("/{id}")
    public CustomerResponseDTO getCustomerById(@PathVariable Long id) {
        return customerService.getCustomerById(id);
    }

    @Operation(summary = "Delete customer by ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
    }

    @Operation(summary = "Update existing customer data")
    @PutMapping("/{id}")
    public CustomerResponseDTO updateCustomer(@Valid @RequestBody CustomerRequestDTO dto, @PathVariable Long id) {
        return customerService.updateCustomer(dto, id);
    }

    @Operation(summary = "Get all customers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "403", description = "You don't have permission")
    })
    @GetMapping
    public List<CustomerResponseDTO> getAllCustomers() {
        return customerService.getAllCustomers();
    }
}
