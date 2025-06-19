package com.example.FinanceTrackerBackend.model.dto.request;

import com.example.FinanceTrackerBackend.constant.TransactionType;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequest {
    @NotBlank(message = "Category name must not be blank")
    private String name;
    private TransactionType type;
}
