package com.example.FinanceTrackerBackend.model.dto.request.filter;

import com.example.FinanceTrackerBackend.constant.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransactionFilter {
    private Long categoryId;
    private TransactionType type;
    private LocalDate fromDate;
    private LocalDate toDate;
    @DecimalMin(value = "0.00", message = "Amount must be at least 0")
    private BigDecimal min;
    @DecimalMin(value = "0.00", message = "Amount must be at least 0")
    private BigDecimal max;
    @Min(0)
    private Integer page = 0;
    private Integer pageSize = 10;
}
