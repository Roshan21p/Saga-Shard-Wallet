package com.example.shardedsagawallet.services.saga;

public interface SagaStep {
    
    boolean execute(SagaContext context); // Executes the step with the provided SagaContext, returning true if successful, false otherwise

    boolean compensate(SagaContext context); // Compensates for the step with the provided SagaContext, returning true if successful, false otherwise

    String getStepName(); // Returns the name of the step, useful for logging and debugging purposes
}
