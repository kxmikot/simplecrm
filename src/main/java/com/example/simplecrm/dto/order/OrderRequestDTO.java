package com.example.simplecrm.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderRequestDTO {
    @NotNull
    private String description;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private Long customerId;
}
