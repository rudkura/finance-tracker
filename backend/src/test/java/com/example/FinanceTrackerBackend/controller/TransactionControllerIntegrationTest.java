package com.example.FinanceTrackerBackend.controller;

import com.example.FinanceTrackerBackend.constant.TransactionType;
import com.example.FinanceTrackerBackend.model.dto.request.AuthenticationRequest;
import com.example.FinanceTrackerBackend.model.dto.request.TransactionRequest;
import com.example.FinanceTrackerBackend.model.dto.response.AuthenticationResponse;
import com.example.FinanceTrackerBackend.model.entity.Category;
import com.example.FinanceTrackerBackend.model.entity.User;
import com.example.FinanceTrackerBackend.repository.CategoryRepository;
import com.example.FinanceTrackerBackend.repository.UserRepository;
import com.example.FinanceTrackerBackend.testutil.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TransactionControllerIntegrationTest {

    @Container
    @ServiceConnection
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("financedbtest")
            .withUsername("root")
            .withPassword("");

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private CategoryRepository categoryRepository;

    private String jwtToken;
    private User user;
    private Category category;

    @BeforeEach
    void setUp() throws Exception {
        // clear db
        categoryRepository.deleteAll();
        userRepository.deleteAll();

        // User
        user = TestDataFactory.user("test@example.com", "Password*123");
        userRepository.save(user);

        // Login and token
        AuthenticationRequest request = TestDataFactory.authenticationRequest("test@example.com", "Password*123");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        AuthenticationResponse response = objectMapper.readValue(result.getResponse().getContentAsString(), AuthenticationResponse.class);
        jwtToken = response.token();

        // Category
        category = TestDataFactory.category("Test category", TransactionType.EXPENSE, user);
        categoryRepository.save(category);
    }

    @Test
    void shouldCreateTransactionForUser() throws Exception {
        TransactionRequest request = TestDataFactory.transactionRequest(new BigDecimal("123.45"), category.getId(), LocalDate.of(2025,5,6), "test transaction");

        mockMvc.perform(post("/api/transactions")
                    .header("Authorization", "Bearer " + jwtToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(123.45))
                .andExpect(jsonPath("$.categoryId").value(category.getId()))
                .andExpect(jsonPath("$.categoryName").value("Test category"))
                .andExpect(jsonPath("$.type").value("EXPENSE"))
                .andExpect(jsonPath("$.date").value("2025-05-06"))
                .andExpect(jsonPath("$.description").value("test transaction"));
    }
}
