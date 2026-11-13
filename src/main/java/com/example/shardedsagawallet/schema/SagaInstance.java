package com.example.shardedsagawallet.schema;

import org.apache.calcite.model.JsonType;

import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

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
@Table (name = "saga_instance")
public class SagaInstance extends BaseEntity {
    
    @Enumerated (EnumType.STRING)
    @Column (name = "status", nullable = false)
    private SagaStatus status = SagaStatus.STARTED;

    @Type (JsonType.class)
    @Column (name = "context", columnDefinition = "json")
    private String context; // JSON string to hold the context of the saga, can be used to store any relevant information

    @Column (name = "current_step", nullable = false)
    private String currentStep; // Name of the current step in the saga, useful for tracking progress and debugging

}