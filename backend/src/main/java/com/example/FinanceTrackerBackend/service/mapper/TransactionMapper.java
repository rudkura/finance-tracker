package com.example.FinanceTrackerBackend.service.mapper;

import com.example.FinanceTrackerBackend.model.dto.response.TransactionSummaryResponse;
import com.example.FinanceTrackerBackend.model.dto.request.TransactionRequest;
import com.example.FinanceTrackerBackend.model.dto.response.TransactionResponse;
import com.example.FinanceTrackerBackend.model.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionResponse toDto(Transaction entity) {
        String desc = entity.getDescription() != null ? entity.getDescription() : "";

        return new TransactionResponse(
                entity.getId(),
                entity.getAmount(),
                entity.getCategory().getId(),
                entity.getCategory().getName(),
                entity.getType(),
                entity.getDate(),
                desc
        );
    }

    public Transaction toEntity(TransactionRequest dto) {
        Transaction entity = new Transaction();
        entity.setAmount(dto.getAmount());
        entity.setDate(dto.getDate());
        entity.setDescription(dto.getDescription());
        return entity;
        // category, type, user set separately in service
    }

    public void update(TransactionRequest source, Transaction target) {
        target.setAmount(source.getAmount());
        target.setDate(source.getDate());
        target.setDescription(source.getDescription());
    }

    public TransactionSummaryResponse toSummaryDTO(Transaction entity) {
        return new TransactionSummaryResponse(
                entity.getId(),
                entity.getCategory().getName(),
                entity.getAmount(),
                entity.getDate()
        );

    }
}
