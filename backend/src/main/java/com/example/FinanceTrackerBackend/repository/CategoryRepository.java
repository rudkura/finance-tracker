package com.example.FinanceTrackerBackend.repository;

import com.example.FinanceTrackerBackend.constant.TransactionType;
import com.example.FinanceTrackerBackend.model.entity.Category;
import com.example.FinanceTrackerBackend.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUser(User user);
    List<Category> findByUserAndType(User user, TransactionType type);
    Optional<Category> findByIdAndUserId(Long id, Long userId);
    boolean existsByNameAndTypeAndUserId(String name, TransactionType type, Long userId);

    @Query("SELECT c.id FROM Category c WHERE c.id IN :ids AND c.user.id = :userId")
    List<Long> findAuthorizedIds(List<Long> ids, Long userId);
}
