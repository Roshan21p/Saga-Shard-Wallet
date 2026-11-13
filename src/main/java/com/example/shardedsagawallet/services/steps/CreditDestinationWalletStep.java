package com.example.shardedsagawallet.services.steps;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.example.shardedsagawallet.repositories.WalletRepository;
import com.example.shardedsagawallet.schema.Wallet;
import com.example.shardedsagawallet.services.saga.SagaContext;
import com.example.shardedsagawallet.services.saga.SagaStep;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service 
@RequiredArgsConstructor 
@Slf4j 
public class CreditDestinationWalletStep implements SagaStep {

    private final WalletRepository walletRepository; // Repository for accessing wallet data, injected via constructor

    @Override 
    @Transactional 
    public boolean execute(SagaContext context) {
        log.info("Executing CreditDestinationWalletStep with context: {}", context);
       
        // Step 1 : Get the destination wallet ID and amount from the context
        Long toWalletId = context.getLong("toWalletId");
        BigDecimal amount = context.getBigDecimal("amount");

        log.info("Crediting destination wallet with ID {} with amount {}", toWalletId, amount);

        // Step 2: Fetch the destination wallet from the database with a pessimistic lock to prevent concurrent modifications
        Wallet wallet = walletRepository.findbyIdWithLock(toWalletId)
                .orElseThrow(() -> new RuntimeException("Destination wallet not found"));
        // This is a placeholder for the actual database query

        log.info("Wallet fetched with balance {}", wallet.getBalance());
        context.put("originalToWalletBalance", wallet.getBalance()); // Store the balance before crediting for potential compensation

        // Step 3: Credit the destination wallet
        wallet.credit(amount);
        walletRepository.save(wallet); // Save the updated wallet back to the database
        log.info("Destination wallet credited successfully. New balance: {}", wallet.getBalance());
        context.put("toWalletBalanceAfterCredit", wallet.getBalance()); // Store the new balance after crediting for potential compensation

        return true; // Return true if successful, false otherwise
    }

    @Override
    @Transactional
    public boolean compensate(SagaContext context) {
        log.info("Compensating CreditDestinationWalletStep with context: {}", context);

        // Step 1: Get the destination wallet ID and the original balance from the context
        Long toWalletId = context.getLong("toWalletId");
        BigDecimal amount =  context.getBigDecimal("amount");

        log.info("Reverting destination wallet with ID {} to original balance {}", toWalletId, amount);

        // Step 2: Fetch the destination wallet from the database with a pessimistic lock
        Wallet wallet = walletRepository.findbyIdWithLock(toWalletId)
                .orElseThrow(() -> new RuntimeException("Destination wallet not found for compensation"));

        context.put("toWalletBalanceBeforeCompensation", wallet.getBalance()); // Store the balance after compensation for potential further actions

        // Step 3: Revert the destination wallet to its original balance
        wallet.credit(amount); // Revert the credit by crediting the original amount back to the wallet
        walletRepository.save(wallet); // Save the reverted wallet back to the database
        
        log.info("Destination wallet reverted successfully. Current balance: {}", wallet.getBalance());
        context.put("toWalletBalanceAfterCompensation", wallet.getBalance()); // Store the balance after compensation for potential further actions

        log.info("Credit compensation of destination wallet step exexuted successfully");
        return true; // Return true if successful, false otherwise
    }
    
    @Override 
    public String getStepName() {
        return "CreditDestinationWalletStep"; // Return the name of the step for logging and debugging purposes
    }
}
