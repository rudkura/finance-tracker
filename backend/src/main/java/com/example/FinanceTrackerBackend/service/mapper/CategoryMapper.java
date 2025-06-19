package com.example.FinanceTrackerBackend.service.mapper;

import com.example.FinanceTrackerBackend.model.dto.request.CategoryRequest;
import com.example.FinanceTrackerBackend.model.dto.response.CategoryResponse;
import com.example.FinanceTrackerBackend.model.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryRequest dto) {
        Category entity = new Category();
        entity.setName(dto.getName());
        entity.setType(dto.getType());
        return entity;
    }

    public CategoryResponse toDto(Category entity) {
        return new CategoryResponse(
                entity.getId(),
                entity.getName(),
                entity.getType()
        );
    }

    public void update(CategoryRequest source, Category target) {
        target.setName(source.getName());
        // type should not change to avoid inconsistency
    }
}
