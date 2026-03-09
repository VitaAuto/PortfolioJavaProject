package org.example.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class OrderRequestDto {
    @NotBlank(message = "User name is required")
    private String username;
    private String description;
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Double amount;
}