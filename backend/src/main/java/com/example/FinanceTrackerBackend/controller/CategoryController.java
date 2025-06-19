package com.example.FinanceTrackerBackend.controller;

import com.example.FinanceTrackerBackend.constant.TransactionType;
import com.example.FinanceTrackerBackend.model.dto.request.CategoryRequest;
import com.example.FinanceTrackerBackend.model.dto.response.CategoryListsResponse;
import com.example.FinanceTrackerBackend.model.dto.response.CategoryResponse;
import com.example.FinanceTrackerBackend.model.entity.User;
import com.example.FinanceTrackerBackend.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Categories", description = "CRUD operations on categories")
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @Operation(summary = "Add category")
    @ApiResponse(responseCode = "200", description = "Category created")
    public ResponseEntity<CategoryResponse> addCategory(@Valid @RequestBody CategoryRequest dto, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(
                categoryService.addCategory(dto, user)
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID")
    @ApiResponse(responseCode = "200", description = "Category found")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(
                categoryService.viewCategory(id, user)
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update category by ID", description = "Change category name")
    @ApiResponse(responseCode = "200", description = "Category updated")
    public ResponseEntity<CategoryResponse> updateCategory(@Valid @RequestBody CategoryRequest dto, @PathVariable Long id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(
                categoryService.updateCategory(id, dto, user)
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category by ID")
    @ApiResponse(responseCode = "204", description = "Category deleted")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id, @AuthenticationPrincipal User user) {
        categoryService.deleteCategory(id, user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "List all categories by type", description = "Optional filtering by category transaction type")
    @ApiResponse(responseCode = "200", description = "")
    public ResponseEntity<CategoryListsResponse> getCategories(@AuthenticationPrincipal User user, @RequestParam(required = false)TransactionType type) {
        return ResponseEntity.ok(
                categoryService.listCategories(user, type)
        );
    }
}
