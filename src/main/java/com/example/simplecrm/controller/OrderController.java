package com.example.simplecrm.controller;

import com.example.simplecrm.api.ApiResponse;
import com.example.simplecrm.dto.order.OrderRequestDTO;
import com.example.simplecrm.dto.order.OrderResponseDTO;
import com.example.simplecrm.model.OrderStatus;
import com.example.simplecrm.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "Operations with customer orders")
public class OrderController {

    private final OrderService orderService;
    public OrderController(final OrderService orderService) { this.orderService = orderService; }

    @Operation(summary = "Create new order for customer")
    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponseDTO>> createOrder(@Valid @RequestBody OrderRequestDTO request) {
        OrderResponseDTO response = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<OrderResponseDTO>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Order created successfully")
                        .data(response)
                        .build()
        );
    }

    @Operation(summary = "Get all orders for customer")
    @GetMapping("/{customerId}/orders")
    public ResponseEntity<ApiResponse<List<OrderResponseDTO>>> getAllOrders(@PathVariable Long customerId) {
        List<OrderResponseDTO> response = orderService.getOrdersByCustomerId(customerId);
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.<List<OrderResponseDTO>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Orders fetched successfully")
                        .data(response)
                        .build()
        );
    }

    @Operation(summary = "Update order status (NEW, PROCESSING, COMPLETED, CANCELED)")
    @PutMapping("/customer/{customerId}/status")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> updateOrderStatus(@RequestParam Long orderId, @PathVariable Long customerId, @RequestParam OrderStatus orderStatus) {
        OrderResponseDTO response = orderService.updateOrderStatus(orderId, orderStatus, customerId);
        return ResponseEntity.ok(
                ApiResponse.<OrderResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("Order status updated successfully")
                        .data(response)
                        .build()
        );
    }
}
