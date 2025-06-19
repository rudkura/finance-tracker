package com.example.FinanceTrackerBackend.repository.specification;

import com.example.FinanceTrackerBackend.model.entity.Category;
import com.example.FinanceTrackerBackend.model.entity.Transaction;
import com.example.FinanceTrackerBackend.model.entity.User;
import com.example.FinanceTrackerBackend.model.dto.request.filter.TransactionFilter;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class TransactionSpecification implements Specification<Transaction> {
    private final TransactionFilter filter;
    private final User user;
    private final Category category;

    @Override
    public Predicate toPredicate(Root<Transaction> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(root.get("user").get("id"), user.getId()));

        if (category != null) {
            predicates.add(cb.equal(root.get("category").get("id"), category.getId()));
        }
        if (filter.getType() != null) {
            predicates.add(cb.equal(root.get("type"), filter.getType()));
        }
        if (filter.getFromDate() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("date"), filter.getFromDate()));
        }
        if (filter.getToDate() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("date"), filter.getToDate()));
        }
        if (filter.getMin() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("amount"), filter.getMin()));
        }
        if (filter.getMax() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("amount"), filter.getMax()));
        }
        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
