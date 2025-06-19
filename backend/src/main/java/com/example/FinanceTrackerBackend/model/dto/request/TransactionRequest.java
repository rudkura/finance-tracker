package com.example.FinanceTrackerBackend.model.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class TransactionRequest {
    @NotNull(message = "Amount must not be empty")
    @DecimalMin(value = "0.00", message = "Amount must be at least 0")
    private BigDecimal amount;
    @NotNull(message = "Category must not be empty")
    private Long categoryId;
    private LocalDate date;
    private String description;
}
