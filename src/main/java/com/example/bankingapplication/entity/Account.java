package com.example.bankingapplication.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.processing.Pattern;
import org.javamoney.moneta.Money;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String number;

    /**
     * We are definitely mustn't use primitive types here because they can cause errors when
     * computing numbers with floating points. Although we have BigDecimal for cases like bank
     * application, I think it is good idea to keep currency and amount of money together. Also
     * class Money uses BigDecimal inside it, and can be widely configured if needed.
     */
    @Column(nullable = false)
    private Money balance;
}
