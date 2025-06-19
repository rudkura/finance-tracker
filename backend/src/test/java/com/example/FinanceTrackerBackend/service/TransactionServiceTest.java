package com.example.FinanceTrackerBackend.service;

import com.example.FinanceTrackerBackend.constant.TransactionType;
import com.example.FinanceTrackerBackend.exception.ApiException;
import com.example.FinanceTrackerBackend.exception.constant.ErrorCode;
import com.example.FinanceTrackerBackend.model.dto.request.TransactionRequest;
import com.example.FinanceTrackerBackend.model.dto.response.TransactionResponse;
import com.example.FinanceTrackerBackend.model.entity.Category;
import com.example.FinanceTrackerBackend.model.entity.Transaction;
import com.example.FinanceTrackerBackend.model.entity.User;
import com.example.FinanceTrackerBackend.repository.CategoryRepository;
import com.example.FinanceTrackerBackend.repository.TransactionRepository;
import com.example.FinanceTrackerBackend.service.mapper.TransactionMapper;
import com.example.FinanceTrackerBackend.testutil.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    /// === Mock dependencies ===
    @Mock private TransactionRepository transactionRepository;
    @Mock private CategoryRepository categoryRepository;
    @Mock private TransactionMapper mapper;

    // === Test dependency ===
    @InjectMocks private TransactionService transactionService;

    // === TESTS FOR: addTransaction(...) ===

    @Test
    public void addTransaction_shouldSaveTransactionAndReturnDto() {

        // === Arrange ===
        // User requesting add
        User user = TestDataFactory.user(1L);

        // category belonging to user
        Category category = TestDataFactory.category(2L, user, "Test category", TransactionType.EXPENSE);

        // request by user
        TransactionRequest request = TestDataFactory.transactionRequest(new BigDecimal("123.45"), 2L, LocalDate.of(2025,6,6), "Test transaction");

        // entity returned by mapper
        Transaction mappedEntity = TestDataFactory.transaction(new BigDecimal("123.45"), "Test transaction", LocalDate.of(2025,6,6));

        // entity returned by repository
        Transaction savedEntity = TestDataFactory.transaction(new BigDecimal("123.45"), "Test transaction", LocalDate.of(2025, 6,6));
        savedEntity.setId(100L);
        savedEntity.setUser(user);
        savedEntity.setCategory(category);
        savedEntity.setType(category.getType());

        // expected response
        TransactionResponse expected = new TransactionResponse(100L, new BigDecimal("123.45"), 2L, "Test category", TransactionType.EXPENSE, LocalDate.of(2025,6,6), "Test transaction");

        // Mocks
        when(mapper.toEntity(request)).thenReturn(mappedEntity);
        when(categoryRepository.findByIdAndUserId(2L, 1L)).thenReturn(Optional.of(category));
        when(transactionRepository.save(mappedEntity)).thenReturn(savedEntity);
        when(mapper.toDto(savedEntity)).thenReturn(expected);

        /// === Act ===
        TransactionResponse actual = transactionService.addTransaction(request, user);

        // === Assert ===

        assertThat(actual).isEqualTo(expected);

        // entity being saved matches
        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(captor.capture());
        Transaction capturedEntity = captor.getValue();
        assertThat(capturedEntity.getCategory()).isEqualTo(category);
        assertThat(capturedEntity.getType()).isEqualTo(category.getType());
        assertThat(capturedEntity.getUser()).isEqualTo(user);

        verify(mapper).toEntity(request);
        verify(mapper).toDto(savedEntity);
    }

    @Test
    public void addTransaction_shouldNotChangeDateIfPresent() {
        // === Arrange ===
        User user = TestDataFactory.user(1L);
        LocalDate originalDate = LocalDate.of(2025,6,6);

        TransactionRequest request = TestDataFactory.transactionRequest(originalDate, 999L);

        when(mapper.toEntity(any())).thenReturn(new Transaction());
        when(categoryRepository.findByIdAndUserId(999L, 1L)).thenReturn(Optional.of(new Category()));

        // === Act ===
        transactionService.addTransaction(request, user);

        // === Assert ===
        ArgumentCaptor<TransactionRequest> captor = ArgumentCaptor.forClass(TransactionRequest.class);
        verify(mapper).toEntity(captor.capture());
        assertThat(captor.getValue().getDate()).isEqualTo(originalDate);
    }

    @Test
    public void addTransaction_shouldSetCurrentDateIfMissing() {
        // === Arrange ===
        User user = TestDataFactory.user(1L);

        TransactionRequest request = TestDataFactory.transactionRequest(null, 999L);

        when(mapper.toEntity(any())).thenReturn(new Transaction());
        when(categoryRepository.findByIdAndUserId(999L, 1L)).thenReturn(Optional.of(new Category()));

        /// === Act ===
        transactionService.addTransaction(request, user);

        /// === Assert ===
        ArgumentCaptor<TransactionRequest> captor = ArgumentCaptor.forClass(TransactionRequest.class);
        verify(mapper).toEntity(captor.capture());
        assertThat(captor.getValue().getDate()).isEqualTo(LocalDate.now());

    }

    @Test
    public void addTransaction_shouldThrowIfCategoryNotFound() {
        // === Arrange ===
        User user = TestDataFactory.user(1L);
        TransactionRequest request = new TransactionRequest();
        request.setCategoryId(999L);

        when(categoryRepository.findByIdAndUserId(999L, 1L)).thenReturn(Optional.empty());

        // === Assert ===
        assertThatThrownBy(() -> transactionService.addTransaction(request, user))
                .isInstanceOf(ApiException.class)
                .extracting(e -> ((ApiException) e).getCode())
                .isEqualTo(ErrorCode.CATEGORY_NOT_FOUND);
    }

    // === TESTS FOR: getTransaction(...) ===

    // TODO TEST: getTransaction_shouldReturnTransactionResponse() {}

    // TODO TEST: getTransaction_shouldThrowIfNotFound() {}

    // === TESTS FOR: updateTransaction(...) ===

    // TODO TEST: updateTransaction_shouldUpdateAndReturnTransactionResponse() {}

    // TODO TEST: updateTransaction_shouldThrowIfTransactionNotFound() {}

    // TODO TEST: updateTransaction_shouldThrowIfCategoryNotFound() {}

    // === TESTS FOR: deleteTransaction(...) ===

    // TODO TEST: deleteTransaction_shouldDeleteTransaction() {}

    // TODO TEST: deleteTransaction_shouldThrowIfNotFound() {}

    // === TEST FOR: getFilteredSummary(...) ===

    // TODO TEST: getFilteredSummary_shouldReturnPagedResponse() {}

    // TODO TEST: getFilteredSummary_shouldThrowIfCategoryNotFound() {}

    // TODO TEST: getFilteredSummary_shouldLimitPageSizeToMax() {}

    // TODO TEST: getFilteredSummary_shouldAcceptNullCategoryId() {}


    // === Arrange ===
    // === Act ===
    // === Assert ===
}
