package com.example.FinanceTrackerBackend.testutil;

import com.example.FinanceTrackerBackend.constant.TransactionType;
import com.example.FinanceTrackerBackend.model.dto.request.AuthenticationRequest;
import com.example.FinanceTrackerBackend.model.dto.request.TransactionRequest;
import com.example.FinanceTrackerBackend.model.entity.Category;
import com.example.FinanceTrackerBackend.model.entity.Transaction;
import com.example.FinanceTrackerBackend.model.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.testcontainers.shaded.org.bouncycastle.pqc.crypto.util.PQCOtherInfoGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TestDataFactory {

    public static User user(Long id) {
        User user = new User();
        user.setId(id);
        return user;
    }

    public static User user(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        return user;
    }

    public static AuthenticationRequest authenticationRequest(String email, String password) {
        AuthenticationRequest req = new AuthenticationRequest();
        req.setEmail(email);
        req.setPassword(password);
        return req;
    }

    public static Category category(Long id, User user, String name, TransactionType type) {
        Category category = new Category();
        category.setId(id);
        category.setUser(user);
        category.setName(name);
        category.setType(type);
        return category;
    }

    public static Category category(String name, TransactionType type, User user) {
        Category category = new Category();
        category.setName(name);
        category.setType(type);
        category.setUser(user);
        return category;
    }

    public static TransactionRequest transactionRequest(LocalDate date, Long categoryId) {
        TransactionRequest request = new TransactionRequest();
        request.setCategoryId(categoryId);
        request.setDate(date);
        return  request;
    }

    public static TransactionRequest transactionRequest(BigDecimal amount, Long categoryId, LocalDate date, String description) {
        TransactionRequest request = new TransactionRequest();
        request.setAmount(amount);
        request.setCategoryId(categoryId);
        request.setDate(date);
        request.setDescription(description);
        return request;
    }

    public static Transaction transaction(BigDecimal amount, String description, LocalDate date) {
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription(description);
        transaction.setDate(date);
        return transaction;
    }
}
