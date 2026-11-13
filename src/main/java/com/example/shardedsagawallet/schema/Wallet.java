package com.example.shardedsagawallet.schema;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity 
@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@Table (name = "wallet")
public class Wallet extends BaseEntity {

    @Column (name = "user_id", nullable = false)
    private Long userId; // Foreign key to the User entity, linking this wallet to its owner

    @Column (name = "is_active", nullable = false)
    private Boolean isActive; // Indicates whether the wallet is active or not, useful for enabling/disabling wallet functionality

    @Column (name = "balance", nullable = false, precision = 10, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO; // Represents the current balance of the wallet, useful for tracking funds and transactions

    public boolean hasSufficientBalance(BigDecimal amount) {
        return balance.compareTo(amount) >= 0; // Checks if the wallet has sufficient balance for a given amount, useful for validating transactions
    }

    public void debit(BigDecimal amount) {
        if (!hasSufficientBalance(amount)) {
            throw new IllegalArgumentException("Insufficient balance in wallet"); // Throws an exception if there is not enough balance, useful for error handling
        }
        balance = balance.subtract(amount); // Deducts the specified amount from the wallet's balance, useful for processing payments or withdrawals
    }

    public void credit(BigDecimal amount) {
        balance = balance.add(amount); // Adds the specified amount to the wallet's balance, useful for processing deposits or refunds
    }
}
