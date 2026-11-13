package com.example.shardedsagawallet.services.saga;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
public class SagaContext {
    
    private Map<String, Object> data; // A map to hold any relevant data for the saga, allowing for flexible storage of information needed across different steps of the saga

    public SagaContext(Map<String, Object> data) {
        this.data = data != null ? data : new HashMap<>(); // Initialize the data map, ensuring it's not null to avoid NullPointerExceptions during saga execution
    }

    public void put(String key, Object value) {
        data.put(key, value); // Add a key-value pair to the context, allowing for dynamic storage of information needed for the saga's execution
    }

    public Object get(String key) {
        return data.get(key); // Retrieve a value from the context based on the provided key, enabling access to stored information during saga execution
    }

    public Long getLong(String key) {
        Object value = data.get(key);
        return value instanceof Number ? ((Number) value).longValue() : null; // Safely retrieve a Long value from the context, returning null if the value is not of type Long
    }

    public BigDecimal getBigDecimal(String key) {
        Object value = data.get(key);
        return value instanceof Number ? BigDecimal.valueOf(((Number) value).doubleValue()) : null; // Safely retrieve a BigDecimal value from the context, returning null if the value is not of type BigDecimal
    }
}
