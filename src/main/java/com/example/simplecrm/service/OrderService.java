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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.orderMapper = orderMapper;
    }

    public OrderResponseDTO createOrder(OrderRequestDTO request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException(request.getCustomerId()));

        Order order = orderMapper.toEntity(request);
        order.setId(null);
        order.setCustomer(customer);

        return orderMapper.toDTO(orderRepository.save(order));
    }

    public List<OrderResponseDTO> getOrdersByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId)
                .stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }

    public OrderResponseDTO updateOrderStatus(Long orderId, OrderStatus status, Long customerId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (!order.getCustomer().getId().equals(customerId)) {
            throw new UnauthorizedAccessException();
        }

        order.setStatus(status);
        return orderMapper.toDTO(orderRepository.save(order));
    }
}
