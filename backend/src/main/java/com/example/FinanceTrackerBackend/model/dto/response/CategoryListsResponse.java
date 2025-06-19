package com.example.FinanceTrackerBackend.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CategoryListsResponse(
        List<CategoryResponse> expense,
        List<CategoryResponse> income
) {}
