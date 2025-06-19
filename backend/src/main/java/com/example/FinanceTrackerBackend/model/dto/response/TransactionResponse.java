package com.example.FinanceTrackerBackend.model.dto.response;

import com.example.FinanceTrackerBackend.constant.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionResponse(
        Long id,
        BigDecimal amount,
        Long categoryId,
        String categoryName,
        TransactionType type,
        LocalDate date,
        String description
) {}
