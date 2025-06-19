package com.example.FinanceTrackerBackend.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CategoryStatsRequest {
    @NotNull
    List<Long> categoryIds;

    @NotNull
    LocalDate from;

    @NotNull
    LocalDate to;
}
