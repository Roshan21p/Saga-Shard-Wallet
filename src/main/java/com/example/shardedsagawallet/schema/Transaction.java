package com.example.shardedsagawallet.schema;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity 
@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@Table (name = "transactions")
public class Transaction extends BaseEntity {

    @Column (name = "from_wallet_id", nullable = false)
    private Long fromWalletId;

    @Column (name = "to_wallet_id", nullable = false)
    private Long toWalletId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Enumerated (EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType = TransactionType.TRANSFER;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_status", nullable = false)
    private TransactionStatus transactionStatus = TransactionStatus.PENDING;

    @Column (name = "description", nullable = false)
    private String description;

    @Column (name = "saga_instance_id", nullable = false)
    private Long sagaInstanceId; // ID of the saga instance associated with this transaction, if applicable
    
}
