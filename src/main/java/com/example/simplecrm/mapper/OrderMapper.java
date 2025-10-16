package com.example.simplecrm.mapper;

import com.example.simplecrm.dto.order.OrderRequestDTO;
import com.example.simplecrm.dto.order.OrderResponseDTO;
import com.example.simplecrm.model.Order;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OrderMapper {
    private final ModelMapper mapper;

    public Order toEntity(OrderRequestDTO request) {
        return mapper.map(request, Order.class);
    }

    public OrderResponseDTO toDTO(Order order) {
        return mapper.map(order, OrderResponseDTO.class);
    }
}
