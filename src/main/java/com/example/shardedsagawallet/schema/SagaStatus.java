package com.example.shardedsagawallet.schema;

public enum SagaStatus {
   STARTED,
   RUNNING,
    COMPLETED,
    FAILED,
    COMPENSATING, // Indicates that the saga is in the process of compensating for a failure
    COMPENSATED // Indicates that the saga has successfully compensated for a failure
}
