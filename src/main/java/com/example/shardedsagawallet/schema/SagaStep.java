package com.example.shardedsagawallet.schema;

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
@Table (name = "saga_step")
public class SagaStep extends BaseEntity {
    
    @Column (name = "saga_instance_id", nullable = false)
    private Long sagaInstanceId; // Foreign key to the SagaInstance entity, linking this step to its parent saga instance

    @Column (name = "step_name", nullable = false)
    private String stepName; // Name of the step, useful for identifying the step in logs and debugging

    @Column (name = "status", nullable = false)
    private StepStatus status; // Status of the step, can be used to track progress and handle failures

    @Column (name = "error_message", nullable = true)
    private String errorMessage; // Optional field to store any error messages related to the step, useful for debugging and error handling

    @Column (name = "step_data", nullable = true)
    private String stepData; // JSON string to hold any data related to the step, can be used to store input/output data or any other relevant information
}

