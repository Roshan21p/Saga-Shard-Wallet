package com.example.shardedsagawallet.services.steps;

import org.springframework.stereotype.Service;

import com.example.shardedsagawallet.repositories.TransactionRepository;
import com.example.shardedsagawallet.schema.Transaction;
import com.example.shardedsagawallet.schema.TransactionStatus;
import com.example.shardedsagawallet.services.saga.SagaContext;
import com.example.shardedsagawallet.services.saga.SagaStep;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service 
@RequiredArgsConstructor 
@Slf4j 
public class UpdateTransactionStatus implements SagaStep {
    
    private final TransactionRepository transactionRepository; // Repository for accessing transaction data, injected via constructor

    @Override
    public boolean execute(SagaContext context) {
        log.info("Executing UpdateTransactionStatus with context: {}", context);
        
        // Step 1: Get the transaction ID and new status from the context
        Long transactionId = context.getLong("transactionId");

        log.info("Updating transaction status for transaction ID: {}", transactionId);

        // Step 2: Fetch the transaction from the database
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        context.put("originalTransactionStatus", transaction.getTransactionStatus()); // Store the original status for potential compensation

        transaction.setTransactionStatus(TransactionStatus.SUCCESS);

        transactionRepository.save(transaction); // Save the updated transaction back to the database
        log.info("Transaction status updated successfully for transaction ID: {}", transactionId);

        context.put("updatedTransactionStatus", transaction.getTransactionStatus()); // Store the new status for potential compensation

        log.info("UpdateTransactionStatus step executed successfully");
        
        return true; // Return true if successful, false otherwise
    }

    @Override 
    public boolean compensate(SagaContext context) {
        log.info("Compensating UpdateTransactionStatus with context: {}", context);
        
        // Step 1: Get the transaction ID and the original status from the context
        Long transactionId = context.getLong("transactionId");
        TransactionStatus originalStatus = TransactionStatus.valueOf(context.getString("originalTransactionStatus"));
        log.info("Compensating transaction status for transaction ID: {} to original status: {}", transactionId, originalStatus);

        // Step 2: Fetch the transaction from the database
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        // Step 3: Revert the transaction status to its original value
        transaction.setTransactionStatus(originalStatus);

        transactionRepository.save(transaction); // Save the reverted transaction back to the database
        log.info("Transaction status reverted successfully for transaction ID: {}", transactionId);

        return true; // Return true if successful, false otherwise
    }

    @Override 
    public String getStepName() {
        return "UpdateTransactionStatus"; // Return the name of this saga step
    }
}
