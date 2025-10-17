package com.example.simplecrm.service;

import com.example.simplecrm.dto.CustomerRequestDTO;
import com.example.simplecrm.dto.CustomerResponseDTO;
import com.example.simplecrm.exception.CustomerNotFoundException;
import com.example.simplecrm.mapper.CustomerMapper;
import com.example.simplecrm.model.Customer;
import com.example.simplecrm.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@org.junit.jupiter.api.extension.ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper mapper;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void getCustomerById_ShouldReturnCustomer_WhenExists() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Bob");

        CustomerResponseDTO dto = new CustomerResponseDTO();
        dto.setId(1L);
        dto.setName("Bob");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(mapper.toDTO(customer)).thenReturn(dto);

        CustomerResponseDTO result = customerService.getCustomerById(1L);

        assertThat(result.getName()).isEqualTo(dto.getName());
        verify(customerRepository).findById(1L);
    }

    @Test
    void getCustomerById_ShouldThrowException_WhenCustomerDoesNotExist() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.getCustomerById(1L))
            .isInstanceOf(CustomerNotFoundException.class);
    }

    @Test
    void deleteCustomerById_ShouldDeleteCustomer_WhenExists() {
        Customer customer = new Customer();
        customer.setId(1L);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        customerService.deleteCustomer(1L);

        verify(customerRepository).findById(1L);
        verify(customerRepository).delete(customer);
    }

    @Test
    void deleteCustomerById_ShouldThrowException_WhenCustomerDoesNotExist() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.deleteCustomer(1L));
    }

    @Test
    void updateCustomerById_ShouldUpdateCustomer_WhenExists() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("bob");
        customer.setEmail("old@mail.com");

        CustomerRequestDTO request = new CustomerRequestDTO();
        request.setName("Robert");
        request.setEmail("new@mail.com");

        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(1L);
        updatedCustomer.setName("Robert");
        updatedCustomer.setEmail("new@mail.com");

        CustomerResponseDTO response = new CustomerResponseDTO();
        response.setId(1L);
        response.setName("Robert");
        response.setEmail("new@mail.com");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);
        when(mapper.toDTO(any(Customer.class))).thenReturn(response);

        CustomerResponseDTO result = customerService.updateCustomer(request, 1L);

        verify(customerRepository).save(any(Customer.class));
        assertThat(result.getName()).isEqualTo("Robert");
        assertThat(result.getEmail()).isEqualTo("new@mail.com");
    }

    @Test
    void updateCustomerById_ShouldThrowException_WhenCustomerDoesNotExist() {
        CustomerRequestDTO request = new CustomerRequestDTO();
        request.setName("Bob");
        request.setEmail("test@mail.com");

        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.updateCustomer(request, 1L))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessageContaining("1");

        verify(customerRepository, never()).save(any());
    }

    @Test
    void createCustomer_ShouldCreateCustomer() {
        CustomerRequestDTO request = new CustomerRequestDTO();
        request.setName("Bob");
        request.setEmail("test@mail.com");

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Bob");
        customer.setEmail("test@mail.com");

        CustomerResponseDTO response = new CustomerResponseDTO();
        response.setId(1L);
        response.setName("Bob");
        response.setEmail("test@mail.com");

        when(mapper.toEntity(request)).thenReturn(customer);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(mapper.toDTO(customer)).thenReturn(response);

        CustomerResponseDTO result = customerService.createCustomer(request);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Bob");
        assertThat(result.getEmail()).isEqualTo("test@mail.com");

        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void getAllCustomers_ShouldReturnAllCustomers() {
        Customer customer1 = new Customer();
        customer1.setId(1L);
        customer1.setName("Bob");

        Customer customer2 = new Customer();
        customer2.setId(2L);
        customer2.setName("Nick");

        CustomerResponseDTO response1 = new CustomerResponseDTO();
        response1.setId(1L);
        response1.setName("Bob");

        CustomerResponseDTO response2 = new CustomerResponseDTO();
        response2.setId(2L);
        response2.setName("Nick");

        when(customerRepository.findAll()).thenReturn(List.of(customer1, customer2));
        when(mapper.toDTO(customer1)).thenReturn(response1);
        when(mapper.toDTO(customer2)).thenReturn(response2);

        List<CustomerResponseDTO> result = customerService.getAllCustomers();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result).extracting("name").containsExactlyInAnyOrder("Bob", "Nick");

        verify(customerRepository, times(1)).findAll();
    }
}