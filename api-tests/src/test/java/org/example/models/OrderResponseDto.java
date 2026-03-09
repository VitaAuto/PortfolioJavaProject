package org.example.models;

import lombok.Data;
import org.example.models.enums.OrderStatus;

import java.time.LocalDateTime;

@Data
public class OrderResponseDto {
    private Long id;
    private String username;
    private LocalDateTime createdAt;
    private OrderStatus status;
    private String description;
    private Double amount;
}