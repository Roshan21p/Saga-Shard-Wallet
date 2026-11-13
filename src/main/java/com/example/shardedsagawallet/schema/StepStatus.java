package com.example.shardedsagawallet.schema;

public enum StepStatus {
    PENDING,
    RUNNING,
    COMPLETED,
    FAILED, // Indicates that the step has failed, and may require compensation or error handling
    COMPENSATING, // Indicates that the step is in the process of compensating for a failure
    COMPENSATED, // Indicates that the step has successfully compensated for a failure
    SKIPPED // Indicates that the step was skipped, possibly due to a conditional check or a previous failure
}
