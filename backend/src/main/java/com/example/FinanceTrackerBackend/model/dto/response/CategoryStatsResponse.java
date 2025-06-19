package com.example.FinanceTrackerBackend.model.dto.response;

import com.example.FinanceTrackerBackend.constant.TransactionType;

import java.math.BigDecimal;

public record CategoryStatsResponse(
        Long categoryId,
        String categoryName,
        TransactionType type,
        Long count,
        BigDecimal sum
) {}
