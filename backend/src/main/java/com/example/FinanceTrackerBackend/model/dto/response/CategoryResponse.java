package com.example.FinanceTrackerBackend.model.dto.response;

import com.example.FinanceTrackerBackend.constant.TransactionType;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CategoryResponse(
        long id,
        String name,
        TransactionType type
) {}
