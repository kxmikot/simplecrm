package com.example.simplecrm.dto.order;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderResponseDTO {
    private Long id;
    private String description;
    private BigDecimal amount;
    private String status;
    private LocalDateTime createdAt;
    private Long customerId;
}
