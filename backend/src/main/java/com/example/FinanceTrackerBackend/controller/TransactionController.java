package com.example.FinanceTrackerBackend.controller;

import com.example.FinanceTrackerBackend.model.dto.request.CategoryStatsRequest;
import com.example.FinanceTrackerBackend.model.dto.request.TransactionRequest;
import com.example.FinanceTrackerBackend.model.dto.response.CategoryStatsResponse;
import com.example.FinanceTrackerBackend.model.dto.response.PagedResponse;
import com.example.FinanceTrackerBackend.model.dto.response.TransactionSummaryResponse;
import com.example.FinanceTrackerBackend.model.dto.response.TransactionResponse;
import com.example.FinanceTrackerBackend.model.entity.User;
import com.example.FinanceTrackerBackend.model.dto.request.filter.TransactionFilter;
import com.example.FinanceTrackerBackend.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Transactions", description = "CRUD operations on transactions")
@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@ApiResponse(responseCode = "200", description = "Success")
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping
    @Operation(summary = "Add transaction")
    public ResponseEntity<TransactionResponse> addTransaction(@Valid @RequestBody TransactionRequest dto, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(
                transactionService.addTransaction(dto, user)
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get transaction by ID")
    public ResponseEntity<TransactionResponse> getTransaction(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(
                transactionService.getTransaction(id, user)
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update transaction by ID")
    public ResponseEntity<TransactionResponse> updateTransaction(@PathVariable Long id, @Valid @RequestBody TransactionRequest dto, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(
                transactionService.updateTransaction(id, dto, user)
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete transaction by ID")
    @ApiResponse(responseCode = "204", description = "Transaction deleted")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id, @AuthenticationPrincipal User user) {
        transactionService.deleteTransaction(id, user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Get transaction list", description = "Provides summarised list of transactions, optional filtering")
    public ResponseEntity<PagedResponse<TransactionSummaryResponse>> getTransactions(@Valid TransactionFilter filter, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(
                transactionService.getFilteredSummary(filter, user)
        );
    }

    @GetMapping("/sums")
    @Operation(summary = "Get sums and counts", description = "Provides sum and count for each category in selected date range")
    public ResponseEntity<List<CategoryStatsResponse>> getCategoryStats(@Valid @RequestBody CategoryStatsRequest request, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(
                transactionService.getCategoryStats(user, request)
        );
    }
}
