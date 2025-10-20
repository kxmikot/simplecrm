package com.example.simplecrm.service;

import com.example.simplecrm.dto.order.OrderRequestDTO;
import com.example.simplecrm.dto.order.OrderResponseDTO;
import com.example.simplecrm.exception.CustomerNotFoundException;
import com.example.simplecrm.exception.OrderNotFoundException;
import com.example.simplecrm.exception.UnauthorizedAccessException;
import com.example.simplecrm.mapper.OrderMapper;
import com.example.simplecrm.model.Customer;
import com.example.simplecrm.model.Order;
import com.example.simplecrm.model.OrderStatus;
import com.example.simplecrm.repository.CustomerRepository;
import com.example.simplecrm.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@org.junit.jupiter.api.extension.ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;
    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrder_ShouldCreateOrder_WhenCustomerExists() {
        Long customerId = 1L;
        OrderRequestDTO request = new OrderRequestDTO();
        request.setCustomerId(customerId);

        Customer customer = new Customer();
        customer.setId(customerId);

        Order mapped = new Order();
        mapped.setCustomer(customer);

        Order saved = new  Order();
        saved.setId(100L);
        saved.setCustomer(customer);

        OrderResponseDTO response = new OrderResponseDTO();
        response.setId(saved.getId());
        response.setCustomerId(saved.getCustomer().getId());

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(orderMapper.toEntity(request)).thenReturn(mapped);
        when(orderRepository.save(mapped)).thenReturn(saved);
        when(orderMapper.toDTO(saved)).thenReturn(response);

        OrderResponseDTO result = orderService.createOrder(request);

        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals(customerId, result.getCustomerId());

        verify(customerRepository).findById(customerId);
        verify(orderMapper).toEntity(request);
        verify(orderRepository).save(mapped);
        verify(orderMapper).toDTO(saved);
    }

    @Test
    void createOrder_ShouldThrowException_WhenCustomerNotFound() {
        OrderRequestDTO request = new OrderRequestDTO();
        request.setCustomerId(1L);

        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.createOrder(request))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessageContaining("1");

        verify(orderRepository, never()).save(any());
    }

    @Test
    void getAllOrders_ShouldReturnAllOrders_WhenCustomerExists() {
        Customer customer = new Customer();
        customer.setId(1L);

        Order order1 = new Order();
        order1.setId(100L);
        order1.setCustomer(customer);
        order1.setDescription("1");

        Order order2 = new Order();
        order2.setId(200L);
        order2.setCustomer(customer);
        order2.setDescription("2");

        OrderResponseDTO response1 = new OrderResponseDTO();
        response1.setId(order1.getId());
        response1.setCustomerId(order1.getCustomer().getId());
        response1.setDescription("1");

        OrderResponseDTO response2 = new OrderResponseDTO();
        response2.setId(order2.getId());
        response2.setCustomerId(order2.getCustomer().getId());
        response2.setDescription("2");

        List<OrderResponseDTO> result =  orderService.getOrdersByCustomerId(customer.getId());
        result.add(response1);
        result.add(response2);

        assertNotNull(result);
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).extracting("description").containsExactlyInAnyOrder("1", "2");

        verify(orderRepository, times(1)).findByCustomerId(customer.getId());
    }

    @Test
    void updateOrderStatus_ShouldThrowException_WhenOrderIsNull() {
        Customer customer = new Customer();
        customer.setId(1L);

        Order order = new Order();
        order.setId(100L);
        order.setStatus(OrderStatus.NEW);

        when(orderRepository.findById(order.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.updateOrderStatus(100L, order.getStatus(), customer.getId()))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessageContaining("100");

        verify(orderRepository, never()).save(any());
    }

    @Test
    void updateOrderStatus_ShouldThrowUnauthorized_Exception_WhenCustomerNotFound() {
        Customer other = new Customer();
        other.setId(2L);

        Customer customer = new Customer();
        customer.setId(1L);

        Order order = new Order();
        order.setId(100L);
        order.setCustomer(customer);
        order.setStatus(OrderStatus.NEW);

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.updateOrderStatus(order.getId(), order.getStatus(), other.getId()))
                .isInstanceOf(UnauthorizedAccessException.class);

        verify(orderRepository).findById(order.getId());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void updateOrderStatus_ShouldSuccess_WhenOrderIsUpdated() {
        Customer customer = new Customer();
        customer.setId(1L);

        Order order = new Order();
        order.setId(100L);
        order.setCustomer(customer);
        order.setStatus(OrderStatus.NEW);

        Order updatedOrder = new Order();
        updatedOrder.setId(100L);
        updatedOrder.setCustomer(customer);
        order.setStatus(OrderStatus.PROCESSING);

        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(updatedOrder.getId());
        dto.setCustomerId(updatedOrder.getCustomer().getId());
        dto.setStatus(String.valueOf(order.getStatus()));


        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(updatedOrder);
        when(orderMapper.toDTO(updatedOrder)).thenReturn(dto);

        OrderResponseDTO response = orderService.updateOrderStatus(order.getId(), updatedOrder.getStatus(), customer.getId());

        assertThat(response.getId()).isEqualTo(updatedOrder.getId());
        assertThat(response.getCustomerId()).isEqualTo(customer.getId());
        assertThat(response.getStatus()).isEqualTo(String.valueOf(OrderStatus.PROCESSING));

        verify(orderRepository).findById(order.getId());
        verify(orderRepository).save(argThat(o ->
                o.getId().equals(order.getId()) &&
                        o.getStatus() == updatedOrder.getStatus() &&
                        o.getCustomer().getId().equals(customer.getId())
        ));
        verify(orderMapper).toDTO(updatedOrder);
    }
}
