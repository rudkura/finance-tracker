package com.example.FinanceTrackerBackend.repository;

import com.example.FinanceTrackerBackend.model.dto.response.CategoryStatsResponse;
import com.example.FinanceTrackerBackend.model.entity.Category;
import com.example.FinanceTrackerBackend.model.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {
    Optional<Transaction> findByIdAndUserId(Long id, Long userId);

    Page<Transaction> findAll(Specification<Transaction> spec, Pageable pageable);
    boolean existsByCategory(Category category);

    @Query("""
        SELECT new com.example.FinanceTrackerBackend.model.dto.response.CategoryStatsResponse(
            t.category.id,
            t.category.name,
            t.category.type,
            COUNT(t),
            SUM(t.amount)
        )
        FROM Transaction t
        WHERE t.user.id = :userId
          AND t.date BETWEEN :from AND :to
          AND t.category.id IN :categoryIds
        GROUP BY t.category.id, t.category.name, t.category.type
        """)
    List<CategoryStatsResponse> getStatsPerCategory(Long userId, List<Long> categoryIds, LocalDate from, LocalDate to);

}
