package com.example.FinanceTrackerBackend.model.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionSummaryResponse(
        Long id,
        String categoryName,
        BigDecimal amount,
        LocalDate date
) {}
