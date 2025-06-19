package com.example.FinanceTrackerBackend.service;

import com.example.FinanceTrackerBackend.exception.ApiException;
import com.example.FinanceTrackerBackend.exception.constant.ErrorCode;
import com.example.FinanceTrackerBackend.model.dto.request.CategoryStatsRequest;
import com.example.FinanceTrackerBackend.model.dto.request.TransactionRequest;
import com.example.FinanceTrackerBackend.model.dto.response.CategoryStatsResponse;
import com.example.FinanceTrackerBackend.model.dto.response.PagedResponse;
import com.example.FinanceTrackerBackend.model.dto.response.TransactionSummaryResponse;
import com.example.FinanceTrackerBackend.model.dto.response.TransactionResponse;
import com.example.FinanceTrackerBackend.model.entity.Category;
import com.example.FinanceTrackerBackend.model.entity.Transaction;
import com.example.FinanceTrackerBackend.model.entity.User;
import com.example.FinanceTrackerBackend.model.dto.request.filter.TransactionFilter;
import com.example.FinanceTrackerBackend.repository.CategoryRepository;
import com.example.FinanceTrackerBackend.repository.specification.TransactionSpecification;
import com.example.FinanceTrackerBackend.service.mapper.TransactionMapper;
import com.example.FinanceTrackerBackend.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final int MAX_PAGE_SIZE = 100;

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionMapper mapper;

    public TransactionResponse addTransaction(TransactionRequest dto, User user) {
        dto.setDate(getDateOrNow(dto));
        Category category = getAuthCategory(dto.getCategoryId(), user);
        Transaction entity = mapper.toEntity(dto);
        entity.setCategory(category);
        entity.setType(category.getType());
        entity.setUser(user);

        return mapper.toDto(transactionRepository.save(entity));
    }

    public TransactionResponse getTransaction(Long id, User user) {
        return mapper.toDto(getAuthTransaction(id, user));
    }

    public TransactionResponse updateTransaction(Long id, TransactionRequest dto, User user) {
        dto.setDate(getDateOrNow(dto));
        Transaction entity = getAuthTransaction(id, user);
        Category category = getAuthCategory(dto.getCategoryId(), user);
        mapper.update(dto, entity);
        entity.setCategory(category);
        entity.setType(category.getType());

        return mapper.toDto(transactionRepository.save(entity));
    }

    public void deleteTransaction(Long id, User user) {
        transactionRepository.delete(getAuthTransaction(id, user));
    }

    public PagedResponse<TransactionSummaryResponse> getFilteredSummary(TransactionFilter filter, User user) {
        // if filter has categoryId, set Category, else set null
        Category category = filter.getCategoryId() != null
                ? getAuthCategory(filter.getCategoryId(), user)
                : null;

        int pageSize = Math.min(MAX_PAGE_SIZE, filter.getPageSize());

        Page<Transaction> result = transactionRepository.findAll(
                new TransactionSpecification(filter, user, category),
                PageRequest.of(filter.getPage(), pageSize, Sort.by(Sort.Direction.DESC, "date")));

        return new PagedResponse<>(
                result.map(mapper::toSummaryDTO).getContent(),
                result.getNumber(),
                result.getSize(),
                result.getTotalPages(),
                result.getTotalElements()
        );
    }

    public List<CategoryStatsResponse> getCategoryStats(User user, CategoryStatsRequest r) {
        checkDateRangeValid(r.getFrom(), r.getTo());
        authorizeCategoryIds(r.getCategoryIds(), user);
        return transactionRepository.getStatsPerCategory(user.getId(), r.getCategoryIds(), r.getFrom(), r.getTo());
    }


    // region: private methods

    private void checkDateRangeValid(LocalDate from, LocalDate to) {
        if (from.isAfter(to)) throw new ApiException(ErrorCode.INVALID_DATE_RANGE);
    }

    private void authorizeCategoryIds(List<Long> requestIds, User user) {
        List<Long> authorizedIds = categoryRepository.findAuthorizedIds(requestIds, user.getId());
        if (authorizedIds.size() != requestIds.size()) {
            throw new ApiException(ErrorCode.CATEGORY_NOT_FOUND);
        }
    }

    private LocalDate getDateOrNow(TransactionRequest dto) {
       if (dto.getDate() != null) {
            return dto.getDate();
       }
        return LocalDate.now();
    }

    private Transaction getAuthTransaction(Long id, User user) {
        return transactionRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(()-> new ApiException(ErrorCode.TRANSACTION_NOT_FOUND));
    }

    private Category getAuthCategory(Long id, User user) {
        return categoryRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ApiException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    // endregion
}
