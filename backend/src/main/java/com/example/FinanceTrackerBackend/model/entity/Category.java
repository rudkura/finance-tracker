package com.example.FinanceTrackerBackend.model.entity;

import com.example.FinanceTrackerBackend.constant.TransactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "name", "type"})
})
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "category")
    private List<Transaction> transactions;
}
