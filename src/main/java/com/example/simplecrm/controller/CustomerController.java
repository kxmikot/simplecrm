package com.example.simplecrm.controller;

import com.example.simplecrm.dto.CustomerRequestDTO;
import com.example.simplecrm.dto.CustomerResponseDTO;
import com.example.simplecrm.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import com.example.simplecrm.api.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<CustomerResponseDTO>> createCustomer(@Valid @RequestBody CustomerRequestDTO dto) {
        CustomerResponseDTO response = customerService.createCustomer(dto);
        return ResponseEntity.ok(
                ApiResponse.<CustomerResponseDTO>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Customer created successfully")
                        .data(response)
                        .build()
        );
    }

    @Operation(summary = "Get customer by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponseDTO>> getCustomerById(@PathVariable Long id) {
        CustomerResponseDTO dto = customerService.getCustomerById(id);
        return ResponseEntity.ok(
            ApiResponse.<CustomerResponseDTO>builder()
                    .status(HttpStatus.OK.value())
                    .message("Customer fetched successfully")
                    .data(dto)
                    .build()
        );
    }

    @Operation(summary = "Delete customer by ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Customer deleted successfully")
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update existing customer data")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponseDTO>> updateCustomer(@Valid @RequestBody CustomerRequestDTO dto, @PathVariable Long id) {
        CustomerResponseDTO response = customerService.updateCustomer(dto, id);
        return ResponseEntity.ok(
                ApiResponse.<CustomerResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("Customer updated successfully")
                        .data(response)
                        .build()
        );
    }

    @Operation(summary = "Get all customers")
    @GetMapping
    public ResponseEntity<ApiResponse<List<CustomerResponseDTO>>> getAllCustomers() {
        List<CustomerResponseDTO> customers = customerService.getAllCustomers();

        ApiResponse<List<CustomerResponseDTO>> response = ApiResponse.<List<CustomerResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .message("Customer list")
                .data(customers)
                .build();
        return ResponseEntity.ok(response);
    }
}