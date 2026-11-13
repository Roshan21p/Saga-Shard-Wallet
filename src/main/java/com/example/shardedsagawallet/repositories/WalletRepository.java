package com.example.shardedsagawallet.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.shardedsagawallet.schema.Wallet;

import jakarta.persistence.LockModeType;

@Repository 
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    
    List<Wallet> findByUserId(Long userId);

    // Find a wallet by its ID with a pessimistic lock
    @Lock (LockModeType.PESSIMISTIC_WRITE) // Ensures that the wallet is locked for writing, preventing other transactions from modifying it until the current transaction is complete
    @Query ("SELECT w FROM Wallet w WHERE w.id = :id")
    Optional<Wallet> findbyIdWithLock(Long id); // Custom method to find a wallet by its ID with a pessimistic lock, useful for ensuring data consistency during concurrent transactions    
}
