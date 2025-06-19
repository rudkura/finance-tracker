package com.example.FinanceTrackerBackend.service;

import com.example.FinanceTrackerBackend.constant.TransactionType;
import com.example.FinanceTrackerBackend.exception.ApiException;
import com.example.FinanceTrackerBackend.exception.constant.ErrorCode;
import com.example.FinanceTrackerBackend.model.dto.request.CategoryRequest;
import com.example.FinanceTrackerBackend.model.dto.response.CategoryListsResponse;
import com.example.FinanceTrackerBackend.model.dto.response.CategoryResponse;
import com.example.FinanceTrackerBackend.model.entity.Category;
import com.example.FinanceTrackerBackend.model.entity.User;
import com.example.FinanceTrackerBackend.repository.CategoryRepository;
import com.example.FinanceTrackerBackend.repository.TransactionRepository;
import com.example.FinanceTrackerBackend.service.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;
    private final CategoryMapper mapper;

    public CategoryResponse addCategory(CategoryRequest dto, User user) {
        checkCategoryNameUniqueForUser(dto.getName(), dto.getType(), user);
        Category entity = mapper.toEntity(dto);
        entity.setUser(user);

        return mapper.toDto(
                categoryRepository.save(entity)
        );
    }
    public CategoryResponse viewCategory(Long id, User user) {
        // TODO: expanded version, transaction count?
        return mapper.toDto(
                getAuthCategory(id, user)
        );
    }
    public CategoryResponse updateCategory(Long id, CategoryRequest dto, User user) {
        Category entity = getAuthCategory(id, user);
        if (dto.getType() != null && !dto.getType().equals(entity.getType())) {
            throw new ApiException(ErrorCode.CATEGORY_TYPE_UPDATE_FORBIDDEN);
        }
        mapper.update(dto, entity);
        return mapper.toDto(categoryRepository.save(entity));
    }
    public void deleteCategory(Long id, User user) {
        Category entity = getAuthCategory(id, user);
        if (transactionRepository.existsByCategory(entity)) {
            throw new ApiException(ErrorCode.CATEGORY_HAS_TRANSACTIONS);
        }
        categoryRepository.delete(entity);
    }

    public CategoryListsResponse listCategories(User user, TransactionType type) {
        if (type == null) {
            List<Category> byUser = categoryRepository.findByUser(user);
            return new CategoryListsResponse(
                    byUser.stream().filter(c -> c.getType() == TransactionType.EXPENSE).map(mapper::toDto).toList(),
                    byUser.stream().filter(c -> c.getType() == TransactionType.INCOME).map(mapper::toDto).toList());
        }
        List<CategoryResponse> response = categoryRepository.findByUserAndType(user, type).stream()
                .map(mapper::toDto)
                .toList();

        if (type == TransactionType.EXPENSE) {
            return new CategoryListsResponse(response, null);
        }
        return new CategoryListsResponse(null, response);
    }

    // region: private methods

    private Category getAuthCategory(Long id, User user) {
        return categoryRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ApiException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    private void checkCategoryNameUniqueForUser(String name, TransactionType type, User user) {
        if (categoryRepository.existsByNameAndTypeAndUserId(name, type, user.getId())) {
            throw new ApiException(ErrorCode.CATEGORY_DUPLICATE);
        }
    }

    // endregion
}
