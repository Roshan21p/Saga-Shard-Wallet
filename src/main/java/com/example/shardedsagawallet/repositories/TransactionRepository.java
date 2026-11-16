package com.example.shardedsagawallet.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;

import com.example.shardedsagawallet.schema.Transaction;

@Repository 
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    List<Transaction> findByFromWalletId(Long fromWalletId); // Method to find transactions by the source wallet ID

    List<Transaction> findByToWalletId(Long toWalletId); // Method to find transactions by the destination wallet ID

    @Query ("SELECT t FROM Transaction t WHERE t.fromWalletId = :walletId OR t.toWalletId = :walletId")
    List<Transaction> findByWalletId(@Param ("walletId") Long walletId); // Method to find transactions by either source or destination wallet ID

    List<Transaction> findByTransactionStatus(TransactionStatus transactionStatus); // Method to find transactions by their status

    List<Transaction> findBySagaInstanceId(Long sagaInstanceId); // Method to find transactions by the saga instance ID
}
