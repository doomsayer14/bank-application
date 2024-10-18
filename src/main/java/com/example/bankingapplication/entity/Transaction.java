package com.example.bankingapplication.entity;

import com.example.bankingapplication.entity.enums.TransactionStatus;
import com.example.bankingapplication.entity.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.javamoney.moneta.Money;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    @Column(nullable = false)
    private Money amount;

    @Column
    private Long fromAccount;

    @Column
    private Long toAccount;

    //We can use @PrePersist annotation to catch creation time, but I think and accordingly to
    //single responsibility principle, entity class is not preferable place for business logic
    @Column(nullable = false)
    private LocalDateTime transactionDate;
}
