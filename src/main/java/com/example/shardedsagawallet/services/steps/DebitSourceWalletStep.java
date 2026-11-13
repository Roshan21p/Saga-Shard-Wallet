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
public class DebitSourceWalletStep implements SagaStep {

    private final WalletRepository walletRepository; // Repository for accessing wallet data, injected via constructor

    @Override 
    @Transactional 
    public boolean execute(SagaContext context) {
        log.info("Executing DebitSourceWalletStep with context: {}", context);
        
             // Step 1 : Get the Source wallet ID and amount from the context
        Long fromWalletId = context.getLong("fromWalletId");
        BigDecimal amount = context.getBigDecimal("amount");

        log.info("Debiting source wallet with ID {} with amount {}", fromWalletId, amount);

        // Step 2: Fetch the Source wallet from the database with a pessimistic lock to prevent concurrent modifications
        Wallet wallet = walletRepository.findbyIdWithLock(fromWalletId)
                .orElseThrow(() -> new RuntimeException("Destination wallet not found"));
        // This is a placeholder for the actual database query

        log.info("Wallet fetched with balance {}", wallet.getBalance());
        context.put("originalFromWalletBalance", wallet.getBalance()); // Store the balance before debiting for potential compensation

        // Step 3: Debit the source wallet
        wallet.debit(amount);
        walletRepository.save(wallet); // Save the updated wallet back to the database
        log.info("Source wallet debited successfully. New balance: {}", wallet.getBalance());
        context.put("fromWalletBalanceAfterDebit", wallet.getBalance()); // Store the new balance after debiting for potential compensation
        
        log.info("DebitSourceWalletStep compensated successfully for wallet ID {} with amount {}", fromWalletId, amount);
        return true; // Return true if successful, false otherwise
    }

    @Override 
    @Transactional
    public boolean compensate(SagaContext context) {
        log.info("Compensating DebitSourceWalletStep with context: {}", context);
   
        
        // Step 1: Get the source wallet ID and the original balance from the context
        Long fromWalletId = context.getLong("fromWalletId");
        BigDecimal amount =  context.getBigDecimal("amount");

        log.info("Reverting source wallet with ID {} to original balance {}", fromWalletId, amount);


        // Step 2: Fetch the source wallet from the database with a pessimistic lock
        Wallet wallet = walletRepository.findbyIdWithLock(fromWalletId)
                .orElseThrow(() -> new RuntimeException("Source wallet not found for compensation"));
        
        context.put("fromWalletBalanceBeforeCompensation", wallet.getBalance()); // Store the balance after compensation for potential further actions

        // Step 3: Revert the source wallet to its original balance
        wallet.credit(amount); // Revert the debit by crediting the original amount back to the wallet
        walletRepository.save(wallet); // Save the reverted wallet back to the database
        
        log.info("Source wallet reverted successfully. Current balance: {}", wallet.getBalance());
        context.put("fromWalletBalanceAfterCompensation", wallet.getBalance()); // Store the balance after compensation for potential further actions

        log.info("Debit compensation of source wallet step exexuted successfully");

        return true; // Return true if successful, false otherwise
    }

    @Override
    public String getStepName() {
        return "DebitSourceWalletStep"; // Return the name of this step for logging and debugging purposes
    }
    
}
